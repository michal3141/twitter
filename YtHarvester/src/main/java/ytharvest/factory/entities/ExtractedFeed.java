package ytharvest.factory.entities;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.exceptions.EntryExtractionException;
import ytharvest.factory.exceptions.FeedExtractionException;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionVisitor;
import com.google.gdata.data.Link;
import com.google.gdata.data.youtube.CommentEntry;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.VideoEntry;


/**
 * The Class ExtractedFeed represents one page of a longer feed. It is a wrapper for extracted
 * entities contained in a feed page, providing operations performed on every page, whatever the
 * type of enitities is.
 */
public class ExtractedFeed
{

	/** The Logger object. */
	private static Logger logger = LoggerFactory.getLogger(ExtractedFeed.class);

	/** The address of feed page contained in this object. */
	private String thisFeedLink;

	/** The address of previous page in a feed. */
	private String prevFeedLink;

	/** The address of next page in a feed. */
	private String nextFeedLink;

	/** The entries of a feed page. */
	private Collection<? extends ExtractedEntity> entries;


	/**
	 * Instantiates a new ExtractedFeed and extracts feed's data. Requires an object representing
	 * feed data, obtained from YouTubeService, as an object of class derived form BaseFeed, base
	 * class for all feeds (users, vidoes or comments feeds).
	 * 
	 * @param feed
	 *            the feed page to extract, obtained from YouTubeService.
	 * @throws FeedExtractionException
	 *             thrown when one of the internal entities' extractions fails.
	 */
	public ExtractedFeed(BaseFeed<?, ?> feed) throws FeedExtractionException
	{
		super();
		log().debug("Extracting feed: {}.", feed.getId());

		this.thisFeedLink = extractThisFeedLink(feed);
		this.prevFeedLink = extractPrevFeedLink(feed);
		this.nextFeedLink = extractNextFeedLink(feed);
		this.entries = extractEntries(feed);
	}


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger
	 */
	private Logger log()
	{
		return logger;
	}


	/**
	 * Extracts the address of feed page contained in this object.
	 * 
	 * @param feed
	 *            the feed page to extract, obtained from YouTubeService.
	 * @return the address of feed page contained in this object.
	 */
	private String extractThisFeedLink(BaseFeed<?, ?> feed)
	{
		Link self = feed.getSelfLink();
		if (self == null)
		{
			log().debug("Null self feed link when extracting users feed, current feed id: {}",
					feed.getId());
			return null;
		}
		return self.getHref();
	}


	/**
	 * Extracts the address of next page in a feed.
	 * 
	 * @param feed
	 *            the feed page to extract, obtained from YouTubeService.
	 * @return the address of next page in a feed.
	 */
	private String extractNextFeedLink(BaseFeed<?, ?> feed)
	{
		Link next = feed.getNextLink();
		if (next == null)
		{
			log().debug("Null next feed link when extracting users feed, current feed id: {}",
					feed.getId());
			return null;
		}
		return next.getHref();
	}


	/**
	 * Extracts the address of previous page in a feed.
	 * 
	 * @param feed
	 *            the feed page to extract, obtained from YouTubeService.
	 * @return the address of previous page in a feed.
	 */
	private String extractPrevFeedLink(BaseFeed<?, ?> feed)
	{
		Link prev = feed.getPreviousLink();
		if (prev == null)
		{
			log().debug("Null previous feed link when extracting users feed, current feed id: {}",
					feed.getId());
			return null;
		}
		return prev.getHref();
	}

	// protected abstract Collection<? extends ExtractedEntity> extractEntries(FeedT feed)
	// throws FeedExtractionException;

	/**
	 * The Class EntityMaker serves as a visitor for BaseFeed hierarchy. Its purpose is to visit the
	 * entries contained in a BaseFeed object, and transform them into {@link ExtractedEntity
	 * ExtractedEntities} (namely, one of its subclasses), depending on the type of entries: user
	 * profiles, videos or comments.
	 */
	protected class EntityMaker implements ExtensionVisitor
	{

		/**
		 * The EntityExtractionStoppedException is an exception thrown when extraction of one of
		 * feed entries fails. {@link EntityMaker} implements a specific interface which allows only
		 * exceptions derived from StoppedException to be thrown. This is such an exception. Works
		 * as a wrapper for {@link ytharvest.factory.exceptions.EntryExtractionException}
		 */
		private class EntityExtractionStoppedException extends StoppedException
		{

			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;

			/** The internal exception. */
			private EntryExtractionException exception;


			/**
			 * Instantiates a new EntityExtractionStoppedException.
			 * 
			 * @param e
			 *            the exception to transfer.
			 */
			public EntityExtractionStoppedException(EntryExtractionException e)
			{
				super("Cauth EntryExtractionException.");
				this.exception = e;
			}


			/**
			 * Getter for the internal exception.
			 * 
			 * @return the internal exception.
			 */
			public EntryExtractionException getException()
			{
				return this.exception;
			}

		};

		/** The entity that will be created in {@link #visit(ExtensionPoint, Extension)} function. */
		private ExtractedEntity entity;


		/**
		 * Override of the visit() method in ExtensionVisitor interface. It receives an feed entry,
		 * whose visit() method was called as an Extension object, and transforms it to appropriate
		 * {@link ExtractedEntity} subtype.
		 * 
		 * @see com.google.gdata.data.ExtensionVisitor#visit(com.google.gdata.data.ExtensionPoint,
		 *      com.google.gdata.data.Extension)
		 */
		@Override
		public boolean visit(ExtensionPoint parent, Extension extension) throws StoppedException
		{
			try
			{
				if (parent == null)
				{
					if (extension instanceof UserProfileEntry)
						entity = new ExtractedUser((UserProfileEntry) extension);
					else if (extension instanceof VideoEntry)
						entity = new ExtractedVideo((VideoEntry) extension);
					else if (extension instanceof CommentEntry)
						entity = new ExtractedComment((CommentEntry) extension);
					else
					{
						log().error("EntityMaker received unsupported Extension.");
						throw new StoppedException("EntityMaker received unsupported Extension.");
					}

					return false;
				}
				else
				{
					throw new StoppedException("EntityMaker run against non-top level Extension.");
				}

			}
			catch (EntryExtractionException e)
			{
				throw new EntityExtractionStoppedException(e);
			}
		}


		/*
		 * (non-Javadoc)
		 * 
		 * Unused.
		 * 
		 * @see
		 * com.google.gdata.data.ExtensionVisitor#visitComplete(com.google.gdata.data.ExtensionPoint
		 * )
		 */
		@Override
		public void visitComplete(ExtensionPoint unused) throws StoppedException
		{} // empty intentionally


		/**
		 * Creates an {@link ExtractedEntity}. Calls its visit() method passing self as an visitor
		 * object. This calls {@link #visit(ExtensionPoint, Extension)}, which creates an entity
		 * that this method will return.
		 * 
		 * @param entry
		 *            the entry to transform into {@link ExtractedEntity}.
		 * @return the extracted entity.
		 * @throws EntryExtractionException
		 *             thrown when extraction of an entity fails.
		 */
		public ExtractedEntity makeEntity(BaseEntry<?> entry) throws EntryExtractionException
		{
			try
			{
				entity = null;

				entry.visit(this, null);

				return entity;
			}
			catch (EntityExtractionStoppedException e)
			{
				throw e.getException();
			}
			catch (StoppedException e)
			{
				log().error("EntityMaker caught StoppedException: {}", e);
				return null;
			}
		}
	}


	/**
	 * Extracts the entries of a feed page. Does so by visiting every feed entry with an
	 * {@link EntityMaker}.
	 * 
	 * @param feed
	 *            the feed page to extract, obtained from YouTubeService.
	 * @return the entries of a feed page.
	 * @throws FeedExtractionException
	 *             thrown when one of the internal entities' extractions fails.
	 */
	private Collection<ExtractedEntity> extractEntries(BaseFeed<?, ?> feed)
			throws FeedExtractionException
	{
		ArrayList<ExtractedEntity> entities = new ArrayList<>(feed.getItemsPerPage());
		Collection<EntryExtractionException> errors = Collections.emptyList();

		EntityMaker maker = new EntityMaker();

		for (BaseEntry<?> entry : feed.getEntries())
		{
			try
			{
				entities.add(maker.makeEntity(entry));
			}
			catch (EntryExtractionException e)
			{
				log().warn("Could not extract one of the entries in users feed.", e);
				errors.add(e);
			}
		}

		if (!errors.isEmpty())
			throw new FeedExtractionException(getThisFeedLink(), getPrevFeedLink(),
					getNextFeedLink(), errors, entities);

		return entities;
	}


	/**
	 * Gets the address of feed page contained in this object.
	 * 
	 * @return the address of feed page contained in this object.
	 */
	public String getThisFeedLink()
	{
		return thisFeedLink;
	}


	/**
	 * Gets the address of previous page in a feed.
	 * 
	 * @return the address of previous page in a feed.
	 */
	public String getPrevFeedLink()
	{
		return prevFeedLink;
	}


	/**
	 * Gets the address of next page in a feed.
	 * 
	 * @return the address of next page in a feed.
	 */
	public String getNextFeedLink()
	{
		return nextFeedLink;
	}


	/**
	 * Gets the entries of a feed page.
	 * 
	 * @return the entries of a feed page.
	 */
	public Collection<? extends ExtractedEntity> getEntities()
	{
		return this.entries;
	}

}

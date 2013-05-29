package ytharvest.factory.exceptions;


import java.util.Collection;

import ytharvest.factory.entities.ExtractedEntity;


/**
 * Exception representing an data extraction error during creation of
 * {@link ytharvest.factory.entities.ExtractedFeed} objects.
 * {@link ytharvest.factory.entities.ExtractedFeed ExtractedFeed's} constructor creates a serie of
 * {@link ytharvest.factory.entities.ExtractedEntity} objects (
 * {@link ytharvest.factory.entities.ExtractedUser},
 * {@link ytharvest.factory.entities.ExtractedVideo} or
 * {@link ytharvest.factory.entities.ExtractedComment}, depending on a feed type), whose
 * construction may fail throwing an {@link EntryExtractionException}. This class is a wrapper for
 * entities that were created successfully and exceptions thrown by the rest of entities.
 * 
 * @see ytharvest.factory.entities.ExtractedUser
 * @see ytharvest.factory.entities.ExtractedVideo
 * @see ytharvest.factory.entities.ExtractedComment
 * @see EntryExtractionException
 */
public class FeedExtractionException extends ExtractionException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The address of feed page whose extraction failed. */
	private String thisFeedLink;
	
	/** The address of previous page in a feed. */
	private String prevFeedLink;
	
	/** The address of next page in a feed. */
	private String nextFeedLink;
	
	/** The feed extraction errors. */
	private Collection<EntryExtractionException> extrErrors;
	
	/** The feed entities that were successfully created. */
	private Collection<? extends ExtractedEntity> entities;


	/**
	 * Instantiates a new FeedExtractionException, requiring all of the data created during feed's extraction.
	 * 
	 * @param thisFeedLink
	 *            the address of feed page whose extraction failed.
	 * @param prevFeedLink
	 *            the address of previous page in a feed.
	 * @param nextFeedLink
	 *            the address of next page in a feed.
	 * @param extrErrors
	 *            the feed extraction errors.
	 * @param entities
	 *            the feed entities that were successfully created.
	 */
	public FeedExtractionException(String thisFeedLink, String prevFeedLink, String nextFeedLink,
			Collection<EntryExtractionException> extrErrors,
			Collection<? extends ExtractedEntity> entities)
	{
		super();
		this.thisFeedLink = thisFeedLink;
		this.prevFeedLink = prevFeedLink;
		this.nextFeedLink = nextFeedLink;
		this.extrErrors = extrErrors;
		this.entities = entities;
	}


	/**
	 * Gets the address of feed page whose extraction failed.
	 * 
	 * @return the address of feed page whose extraction failed.
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
	 * Gets the feed extraction errors.
	 * 
	 * @return the feed extraction errors.
	 */
	public Collection<EntryExtractionException> getExtrErrors()
	{
		return extrErrors;
	}


	/**
	 * Gets the feed entities that were successfully created.
	 * 
	 * @return the feed entities that were successfully created.
	 */
	public Collection<? extends ExtractedEntity> getEntities()
	{
		return entities;
	}

}

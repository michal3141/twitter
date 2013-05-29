package ytharvest.factory.dispatchers;


import java.io.IOException;

import ytharvest.factory.EntryFactory;
import ytharvest.factory.entities.ExtractedFeed;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;


/**
 * The CommentFeedDispatcher is a simple class implementing obtaining feed pages that contain
 * comment entries. It does so by calling
 * {@link ytharvest.factory.EntryFactory#getCommentFeed(String)}.
 */
public class CommentFeedDispatcher extends FeedDispatcher
{

	/** The EntryFactory object. */
	private EntryFactory factory;


	/**
	 * Instantiates a new CommentFeedDispatcher.
	 * 
	 * @param factory
	 *            the EntryFactory object.
	 */
	public CommentFeedDispatcher(EntryFactory factory)
	{
		super();
		this.factory = factory;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.dispatchers.FeedDispatcher#getFeedPage(java.lang.String)
	 */
	@Override
	protected ExtractedFeed getFeedPage(String url) throws HarvServiceForbiddenException,
			FeedExtractionException, HarvServiceException, IOException
	{
		return factory.getCommentFeed(url);
	}

}

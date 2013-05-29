package ytharvest.factory.dispatchers;


import java.io.IOException;

import ytharvest.factory.EntryFactory;
import ytharvest.factory.entities.ExtractedFeed;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;


/**
 * The UserFeedDispatcher is a simple class implementing obtaining feed pages that contain user
 * entries. It does so by calling {@link ytharvest.factory.EntryFactory#getUserFeed(String)}.
 */
public class UserFeedDispatcher extends FeedDispatcher
{

	/** The EntryFactory object. */
	private EntryFactory factory;


	/**
	 * Instantiates a new UserFeedDispatcher.
	 * 
	 * @param factory
	 *            the EntryFactory object.
	 */
	public UserFeedDispatcher(EntryFactory factory)
	{
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
		return factory.getUserFeed(url);
	}

}

package ytharvest.factory.dispatchers;


import java.io.IOException;

import ytharvest.factory.EntryFactory;
import ytharvest.factory.entities.ExtractedFeed;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;


/**
 * The VideoFeedDispatcher is a simple class implementing obtaining feed pages that contain video
 * entries. It does so by calling {@link ytharvest.factory.EntryFactory#getVideoFeed(String)}.
 */
public class VideoFeedDispatcher extends FeedDispatcher
{

	/** The EntryFactory object. */
	private EntryFactory factory;


	/**
	 * Instantiates a new VideoFeedDispatcher.
	 * 
	 * @param factory
	 *            the EntryFactory object.
	 */
	public VideoFeedDispatcher(EntryFactory factory)
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
		return factory.getVideoFeed(url);
	}

}

package ytharvest.factory;


import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.entities.ExtractedFeed;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;
import ytharvest.factory.exceptions.UnretrievableUserNameException;
import ytharvest.factory.exceptions.UnretrievableVideoIdException;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.IAtom;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.youtube.CommentFeed;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.UserProfileFeed;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ServiceForbiddenException;


/**
 * A main factory for creating objects representing downloaded YouTube entities (ExtractedEntity
 * hierarchy).
 * <br><br>
 * 
 * EntryFactory is the main object of YtHarvester library used to interact with YouTube. It contains
 * high-level functions that allow to download YouTube entities in the form of convenient
 * {@link ytharvest.factory.entities.ExtractedEntity} objects. It can be used by end users.
 * <br><br>
 * 
 * To create EntryFactory, one needs to pass an instantiated YouTubeService object (an main element
 * of GData library for interacting with YouTube). It is usually more convenient to configure
 * {@link ytharvest.properties.Properties} and then use {@link ytharvest.factory.EntryFactoryFacade}
 * , which uses EntryFactory internally.
 */
public class EntryFactory
{

	/**
	 * The sleep time (used when server replies 'too many recent calls', see
	 * {@link #getEntity(String, Class)}).
	 */
	private static int SLEEP_TIME = 3 * 1000;

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(EntryFactory.class);

	/** The YouTubeService reference. */
	private YouTubeService ytService;


	/**
	 * Instantiates a new entry factory.
	 * 
	 * @param service
	 *            The YouTubeService object
	 */
	public EntryFactory(YouTubeService service)
	{
		this.ytService = service;
	}


	/**
	 * Getter for Logger object.
	 * 
	 * @return the logger
	 */
	private Logger log()
	{
		return logger;
	}


	/**
	 * Gets the YouTubeService object.
	 * 
	 * @return the service
	 */
	private YouTubeService getService()
	{
		return ytService;
	}


	/**
	 * Gets the user entry.
	 * 
	 * @param id
	 *            the user's name
	 * @return the user entry, represented as {@link ytharvest.factory.entities.ExtractedUser}
	 *         object.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired user's profile (e.g. private profile).
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws UnretrievableUserNameException
	 *             thrown, when user's name could not have been retrieved from data returned by the
	 *             server. It is assumed, that in such case entire profile is to be discarded.
	 */
	public ExtractedUser getUserEntry(String id) throws HarvServiceForbiddenException,
			HarvServiceException, IOException, UnretrievableUserNameException
	{
		log().debug("Getting user's profile: {}", id);

		UserProfileEntry entry = getEntity(URLCreator.getUserAddress(id), UserProfileEntry.class);

		return new ExtractedUser(entry);
	}


	/**
	 * Gets the video entry.
	 * 
	 * @param id
	 *            the id
	 * @return the video entry, represented as {@link ytharvest.factory.entities.ExtractedVideo}
	 *         object.
	 * @throws UnretrievableVideoIdException
	 *             thrown, when video's id could not have been retrieved from data returned by the
	 *             server. It is assumed, that in such case entire entry is to be discarded.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired user's profile (e.g. private video).
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ExtractedVideo getVideoEntry(String id) throws IOException,
			HarvServiceForbiddenException, HarvServiceException, UnretrievableVideoIdException
	{
		log().debug("Getting video entry: {}", id);

		VideoEntry entry = getEntity(URLCreator.getVideoAddress(id), VideoEntry.class);

		return new ExtractedVideo(entry);
	}


	/**
	 * Gets the user feed (one of its pages).
	 * 
	 * @param url
	 *            address of the feed containing list of users (search results, channel subscribers
	 *            etc.)
	 * @return the user feed, represented as {@link ytharvest.factory.entities.ExtractedFeed} object
	 *         containing collection of internal user profiles.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired user's profile (e.g. private profile).
	 * @throws FeedExtractionException
	 *             thrown when at least one of profile's data extractions throws an
	 *             {@link ytharvest.factory.exceptions.EntryExtractionException}. Such errors may be
	 *             thrown during construction of {@link ytharvest.factory.entities.ExtractedUser}
	 *             objects.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ExtractedFeed getUserFeed(String url) throws IOException, HarvServiceForbiddenException,
			HarvServiceException, FeedExtractionException
	{
		log().debug("Getting users feed: {}", url);

		UserProfileFeed feed = getEntity(url, UserProfileFeed.class);

		return new ExtractedFeed(feed);
	}


	/**
	 * Gets the video feed (one of its pages).
	 * 
	 * @param url
	 *            the address of the feed.
	 * @return the video feed, represented as {@link ytharvest.factory.entities.ExtractedFeed}
	 *         object containing collection of internal videos.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired data.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws FeedExtractionException
	 *             extraction of one of the internal entities failed.
	 */
	public ExtractedFeed getVideoFeed(String url) throws IOException,
			HarvServiceForbiddenException, HarvServiceException, FeedExtractionException
	{
		log().debug("Getting videos feed: {}", url);

		VideoFeed feed = getEntity(url, VideoFeed.class);

		return new ExtractedFeed(feed);
	}


	/**
	 * Gets the comment feed (one of its pages).
	 * 
	 * @param url
	 *            the address of the feed.
	 * @return the comment feed, represented as {@link ytharvest.factory.entities.ExtractedFeed}
	 *         object containing collection of internal comments.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired data.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws FeedExtractionException
	 *             extraction of one of the internal entities failed.
	 */
	public ExtractedFeed getCommentFeed(String url) throws IOException,
			HarvServiceForbiddenException, HarvServiceException, FeedExtractionException
	{
		log().debug("Getting comments feed: {}", url);

		CommentFeed feed = getEntity(url, CommentFeed.class);

		return new ExtractedFeed(feed);
	}


	/**
	 * Gets one entity from YouTube. This function is the main algorithm downloading the data (using
	 * {@link #getObject(URL, Class)} function, recognizing simple errors and translating exceptions
	 * (into {@link ytharvest.factory.exceptions.HarvestException} hierarchy of exceptions).
	 * 
	 * @param <T>
	 *            the type of an entity to download, one of the following GData classes:
	 *            UserProfileEntry, VideoEntry, UserProfileFeed, VideoFeed, CommentFeed.
	 * @param addr
	 *            the address of an entity.
	 * @param clazz
	 *            the Class object representing T's class.
	 * @return the GData object representing downloaded entity. Instance of T.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired data.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private <T extends IAtom> T getEntity(String addr, Class<T> clazz) throws HarvServiceException,
			HarvServiceForbiddenException, IOException
	{
		T entry = null;
		URL url = URLCreator.getUrl(addr);

		do
		{
			try
			{
				entry = getObject(url, clazz);
			}
			catch (ServiceForbiddenException e)
			{
				throw new HarvServiceForbiddenException(e);
			}
			catch (ServiceException se)
			{
				if (isTooManyRecentCallsException(se))
				{
					log().debug("Request quota exceeded, sleeping.");
					try
					{
						Thread.sleep(EntryFactory.SLEEP_TIME);
					}
					catch (InterruptedException ie)
					{
						log().warn("EntryFactory interrupted while sleeping.", ie);
					}
				}
				else
				{
					log().debug("Caught ServiceException.", se);
					throw new HarvServiceException(se);
				}
			}
		}
		while (entry == null);

		return entry;
	}


	/**
	 * Function used to hide the fact, that YouTubeService object has two differrent methods for
	 * downloading single entries and feeds (collections of entries). This function can download
	 * both of them.
	 * 
	 * @param <T>
	 *            the type of an entity to download.
	 * @param url
	 *            entity's address.
	 * @param clazz
	 *            the Class object representing T's class.
	 * @return the GData object representing downloaded entity. Instance of T.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ServiceException
	 *             Signals some GData error.
	 */
	private <T extends IAtom> T getObject(URL url, Class<T> clazz) throws IOException,
			ServiceException
	{
		Object result = null;

		Class<? extends IEntry> entryRef = isIEntry(clazz);
		if (entryRef != null)
			result = getService().getEntry(url, entryRef);

		Class<? extends IFeed> feedRef = isIFeed(clazz);
		if (feedRef != null)
			result = getService().getFeed(url, feedRef);

		if (result == null)
			log().error("Class object not recognized!");

		return clazz.cast(result);
	}


	/**
	 * Checks if argument is one of the IEntry descendants - if so, {@link #getObject(URL, Class)}
	 * will invoke getEntry() on YouTubeService object.
	 * 
	 * @param <T>
	 *            the type of an entity to download, as an subtype of IAtom.
	 * @param clazz
	 *            the Class object representing T's class.
	 * @return the given argument, as an subtype of IEntry (note that this is different than
	 *         argument's type).
	 */
	private <T extends IAtom> Class<? extends IEntry> isIEntry(Class<T> clazz)
	{
		if (clazz.equals(UserProfileEntry.class))
			return UserProfileEntry.class;
		else if (clazz.equals(VideoEntry.class))
			return VideoEntry.class;
		else
			return null;
	}


	/**
	 * Checks if argument is one of the IFeed descendants - if so, {@link #getObject(URL, Class)}
	 * will invoke getFeed() on YouTubeService object.
	 * 
	 * @param <T>
	 *            the type of an entity to download, as an subtype of IAtom.
	 * @param clazz
	 *            the Class object representing T's class.
	 * @return the given argument, as an subtype of IFeed (note that this is different than
	 *         argument's type).
	 */
	private <T> Class<? extends IFeed> isIFeed(Class<T> clazz)
	{
		if (clazz.equals(UserProfileFeed.class))
			return UserProfileFeed.class;
		else if (clazz.equals(VideoFeed.class))
			return VideoFeed.class;
		else if (clazz.equals(CommentFeed.class))
			return CommentFeed.class;
		else
			return null;
	}


	/**
	 * Checks if given ServiceException object represents the 'too many recent calls' error.
	 * 
	 * @param exc
	 *            the exception to check.
	 * @return true if the argument represents the aforementioned error, false otherwise.
	 */
	private boolean isTooManyRecentCallsException(ServiceException exc)
	{
		// Może jakoś bardziej elegancko??
		String response = exc.getResponseBody();

		if (response != null)
			return response.contains("too_many_recent_calls");
		else
			return false;

	}

}

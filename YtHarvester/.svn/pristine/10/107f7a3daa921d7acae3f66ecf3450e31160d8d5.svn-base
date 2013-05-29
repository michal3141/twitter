package ytharvest.factory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.dispatchers.CommentFeedDispatcher;
import ytharvest.factory.dispatchers.FeedDispatcher;
import ytharvest.factory.dispatchers.UserFeedDispatcher;
import ytharvest.factory.dispatchers.VideoFeedDispatcher;
import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;
import ytharvest.factory.exceptions.UnretrievableUserNameException;
import ytharvest.factory.exceptions.UnretrievableVideoIdException;
import ytharvest.factory.filters.FilterFactory;
import ytharvest.properties.Literals;
import ytharvest.properties.Properties;

import com.google.gdata.client.youtube.YouTubeService;


/**
 * Main class of YtHarvester library designed to be used directly by end users. <br>
 * <br>
 * 
 * It is a facade of library's functionality. It contains high-level functions that allow to
 * download YouTube entities in the form of convenient
 * {@link ytharvest.factory.entities.ExtractedEntity} objects. If exact types are needed, two
 * solutions are possible: usage of {@link #castEntities(Collection, Class)} function to cast the
 * entities down to type derived from {@link ytharvest.factory.entities.ExtractedEntity} , or
 * (preferred, used by YtHarvester internally) creation of
 * {@link ytharvest.factory.entities.ExtractedEntityVisitor} object. <br>
 * <br>
 * 
 * This class has only one contructor that does not take any arguments, as all needed parameters are
 * taken from {@link ytharvest.properties.Properties} class. What it means, is that prior to
 * EntryFactoryFacade creation one should configure the library by calling one of the static
 * configure() functions from {@link ytharvest.properties.Properties}.
 * 
 * @see ytharvest.factory.entities.ExtractedEntity
 * @see ytharvest.factory.entities.ExtractedEntityVisitor
 * @see ytharvest.properties.Properties
 */
public class EntryFactoryFacade
{

	/** The logger. */
	private static Logger log = LoggerFactory.getLogger(EntryFactoryFacade.class);

	/** The entry factory. */
	private EntryFactory entryFactory;


	/**
	 * Getter for Logger object.
	 * 
	 * @return the logger
	 */
	private static Logger log()
	{
		return EntryFactoryFacade.log;
	}


	/**
	 * Main contructor.
	 * 
	 * Creates internal {@link ytharvest.factory.EntryFactory} object, which this facade is a
	 * wrapper for. It requires {@link ytharvest.properties.Properties} to be properly configured
	 * prior to facade's creation.
	 */
	public EntryFactoryFacade()
	{
		log().info("Creating EntryFactoryFacade instance.");

		String appName = Properties.get(Literals.HARVESTER_APPNAME);
		// String devId = Properties.getProperty(Literals.DEVELEOPER_KEY);

		log().info("Application name: " + appName);
		// log().info("Developer ID: " + devId);

		YouTubeService serv = new YouTubeService(appName);
		// YouTubeService serv = new YouTubeService(appName, devId);

		entryFactory = new EntryFactory(serv);
	}


	/**
	 * Getter for EntryFactory object.
	 * 
	 * @return the entry factory
	 */
	private EntryFactory factory()
	{
		return entryFactory;
	}


	/**
	 * Function for retrieving user's profile.
	 * 
	 * @param ID
	 *            String object representing user's identifier (nick).
	 * @return ExtractedUser object holding user's profile data.
	 * @throws UnretrievableUserNameException
	 *             thrown, when user's name could not have been retrieved from data returned by the
	 *             server. It is assumed, that in such case entire profile is to be discarded.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired user's profile (e.g. private profile).
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ExtractedUser getUserEntry(String ID) throws UnretrievableUserNameException,
			HarvServiceForbiddenException, HarvServiceException, IOException
	{
		return factory().getUserEntry(ID);
	}


	/**
	 * Function for retrieving video's data.
	 * 
	 * @param ID
	 *            unique video identifier (...&v=ID).
	 * @return ExtractedVideo object holding all of the data about a video.
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
	public ExtractedVideo getVideoEntry(String ID) throws UnretrievableVideoIdException,
			HarvServiceForbiddenException, HarvServiceException, IOException
	{
		return factory().getVideoEntry(ID);
	}


	/**
	 * Gets the list of user profiles contained in given feed.
	 * 
	 * @param URL
	 *            address of the feed containing list of users (search results, channel subscribers
	 *            etc.)
	 * @return collection of {@link ytharvest.factory.entities.ExtractedUser} objects given by a
	 *         reference to their base class. For further manipulation one may use
	 *         {@link #castEntities(Collection, Class) castEntities()} or
	 *         {@link ytharvest.factory.entities.ExtractedEntityVisitor visitor objects}.
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
	public Collection<ExtractedEntity> getUserFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		FeedDispatcher D = new UserFeedDispatcher(factory());

		return D.dispatch(URL, FilterFactory.getUserFilter());
	}


	/**
	 * Gets the list of videos contained in given feed.
	 * 
	 * @param URL
	 *            address of the feed containing list of videos (search results, user's favourites
	 *            etc.)
	 * @return collection of {@link ytharvest.factory.entities.ExtractedVideo} objects given by a
	 *         reference to their base class. For further manipulation one may use
	 *         {@link #castEntities(Collection, Class) castEntities()} or
	 *         {@link ytharvest.factory.entities.ExtractedEntityVisitor visitor objects}.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired video (e.g. private video).
	 * @throws FeedExtractionException
	 *             thrown when at least one of video's data extractions throws an
	 *             {@link ytharvest.factory.exceptions.EntryExtractionException}. Such errors may be
	 *             thrown during construction of {@link ytharvest.factory.entities.ExtractedVideo}
	 *             objects.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Collection<ExtractedEntity> getVideoFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		FeedDispatcher D = new VideoFeedDispatcher(factory());

		return D.dispatch(URL, FilterFactory.getVideoFilter());
	}


	/**
	 * Gets the list of comments contained in given feed (usually obtained by calling
	 * {@link ytharvest.factory.entities.ExtractedVideo#getCommentsLink()}).
	 * 
	 * @param URL
	 *            address of the feed.
	 * @return collection of {@link ytharvest.factory.entities.ExtractedComment} objects given by a
	 *         reference to their base class. For further manipulation one may use
	 *         {@link #castEntities(Collection, Class) castEntities()} or
	 *         {@link ytharvest.factory.entities.ExtractedEntityVisitor visitor objects}.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access desired comments.
	 * @throws FeedExtractionException
	 *             thrown when at least one of comment's data extractions throws an
	 *             {@link ytharvest.factory.exceptions.EntryExtractionException}. Such errors may be
	 *             thrown during construction of {@link ytharvest.factory.entities.ExtractedComment}
	 *             objects.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Collection<ExtractedEntity> getCommentFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		FeedDispatcher D = new CommentFeedDispatcher(factory());

		return D.dispatch(URL, FilterFactory.getCommentFilter());
	}


	/**
	 * Casts down objects in the given collection, to one of
	 * {@link ytharvest.factory.entities.ExtractedEntity ExtractedEntity's} subclasses:
	 * {@link ytharvest.factory.entities.ExtractedUser},
	 * {@link ytharvest.factory.entities.ExtractedVideo} or
	 * {@link ytharvest.factory.entities.ExtractedComment}. It is intended to be a helper function
	 * for obtaining the real type of objects returned by {@link #getUserFeed(String)},
	 * {@link #getVideoFeed(String)} or {@link #getCommentFeed(String)}. However, more elegant
	 * solution would be to create a visitor for {@link ytharvest.factory.entities.ExtractedEntity
	 * ExtractedEntity's} hierarchy, see interface
	 * {@link ytharvest.factory.entities.ExtractedEntityVisitor}.
	 * 
	 * @param c
	 *            collection of objects derived from
	 *            {@link ytharvest.factory.entities.ExtractedEntity}.
	 * @param clazz
	 *            Class object representing target type.
	 * @return collection of objects of type clazz.
	 * @throws ClassCastException
	 *             thrown, when casting fails, that is when given Class object is not of correct
	 *             type.
	 */
	public <T extends ExtractedEntity> Collection<T> castEntities(
			Collection<? extends ExtractedEntity> c, Class<T> clazz) throws ClassCastException
	{
		ArrayList<T> r = new ArrayList<>();
		r.ensureCapacity(c.size());

		for (ExtractedEntity e : c)
			r.add(clazz.cast(e));

		return r;
	}


	/**
	 * Returns list of users in a feed. Performs just as {@link #getUserFeed(String)}, but
	 * additionaly casts returned collection. See {@link #getUserFeed(String)} for description of
	 * required arguments and thrown exceptions. <br>
	 * <br>
	 * 
	 * Attention: invalid casting may be detected when accessing one of the objects in the
	 * collection, long after calling this function. The caller of this function must be sure that
	 * url argument points to a proper user feed. The '@SuppressWarnings("unchecked")' adnotation is
	 * intentional.
	 * 
	 * @return collection of {@link ytharvest.factory.entities.ExtractedUser} objects, being a
	 *         casted result of {@link #getUserFeed(String)}
	 * @throws HarvServiceForbiddenException
	 * @throws FeedExtractionException
	 * @throws HarvServiceException
	 * @throws IOException
	 * @throws ClassCastException
	 *             thrown when casting fails.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ExtractedUser> getCastedUserFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException, ClassCastException
	{
		return (Collection<ExtractedUser>) (Collection<?>) getUserFeed(URL);

	}


	/**
	 * Returns list of videos in a feed. Performs just as {@link #getVideoFeed(String)}, but
	 * additionaly casts returned collection. See {@link #getVideoFeed(String)} for description of
	 * required arguments and thrown exceptions. <br>
	 * <br>
	 * 
	 * Attention: invalid casting may be detected when accessing one of the objects in the
	 * collection, long after calling this function. The caller of this function must be sure that
	 * url argument points to a proper video feed. The '@SuppressWarnings("unchecked")' adnotation
	 * is intentional.
	 * 
	 * @return collection of {@link ytharvest.factory.entities.ExtractedVideo} objects, being a
	 *         casted result of {@link #getVideoFeed(String)}
	 * @throws HarvServiceForbiddenException
	 * @throws FeedExtractionException
	 * @throws HarvServiceException
	 * @throws IOException
	 * @throws ClassCastException
	 *             thrown when casting fails.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ExtractedVideo> getCastedVideoFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		return (Collection<ExtractedVideo>) (Collection<?>) getVideoFeed(URL);
	}


	/**
	 * Returns list of videos in a feed. Performs just as {@link #getCommentFeed(String)}, but
	 * additionaly casts returned collection. See {@link #getCommentFeed(String)} for description of
	 * required arguments and thrown exceptions. <br>
	 * <br>
	 * 
	 * Attention: invalid casting may be detected when accessing one of the objects in the
	 * collection, long after calling this function. The caller of this function must be sure that
	 * url argument points to a proper comment feed. The '@SuppressWarnings("unchecked")' adnotation
	 * is intentional.
	 * 
	 * @return collection of {@link ytharvest.factory.entities.ExtractedComment} objects, being a
	 *         casted result of {@link #getCommentFeed(String)}
	 * @throws HarvServiceForbiddenException
	 * @throws FeedExtractionException
	 * @throws HarvServiceException
	 * @throws IOException
	 * @throws ClassCastException
	 *             thrown when casting fails.
	 */
	@SuppressWarnings("unchecked")
	public Collection<ExtractedComment> getCastedCommentFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		return (Collection<ExtractedComment>) (Collection<?>) getCommentFeed(URL);

	}
}

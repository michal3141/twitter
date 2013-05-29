package ytharvest.factory.entities;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.exceptions.UnretrievableUserNameException;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.YtGender;
import com.google.gdata.data.youtube.YtUserProfileStatistics;


/**
 * The ExtractedUser class represents information about an YouTube user.
 */
public class ExtractedUser extends ExtractedEntity
{

	/**
	 * The retrieval fail marker - some fields may have this value indicating that they should be
	 * considered as not having any value. Analogous to 'null' value for references.
	 */
	public static int retrievalFailMarker = -1;

	/** The Logger object. */
	private static Logger logger = LoggerFactory.getLogger(ExtractedUser.class);

	/** The user's name. */
	private String userName;

	/** The user's profile url. */
	private String profileUrl;

	/** The date when user joined YouTube. */
	private Date joinDate;

	/** The time, when feed representing user data was last updated. */
	private Date lastFeedUpdate;

	/** The time, when user recently accessed their profile via the YouTube website. */
	private Date lastWebAccess;

	/** The time, when user's profile was crawled. */
	private Date crawlDate;

	/** The number of videos uploaded by this user. */
	private int uploadCount;

	/** The number of videos favourited by this user. */
	private int favouritesCount;

	/** The number of users subscribed to this user. */
	private int subscriberCount;

	/** The number of videos watched by this user. */
	private int videoWatchCount;

	/** Indicated how many times this user's uploaded videos were watched. */
	private int totalUploadViews;

	/** True if this user is a male. */
	private boolean isMale;

	/** The user's location around the world. */
	private String location;

	/** The address of this user's uploaded videos feed. */
	private String uploadedVideosLink;

	/** The address of this user's favourites feed. */
	private String favedVideosLink;

	/** The address of this user's subscribed users feed. */
	private String subscriptionsLink;


	/**
	 * Instantiates a new ExtractedUser. Performs the actual extraction of the data.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @throws UnretrievableUserNameException
	 *             thrown when user's name could not have been extracted.
	 */
	public ExtractedUser(UserProfileEntry entry) throws UnretrievableUserNameException
	{
		log().debug("Extracting user entry.");

		YtUserProfileStatistics stats = extractProfileStats(entry);

		this.userName = extractUserName(entry);
		this.profileUrl = extractProfileUrl(entry);
		this.joinDate = extractJoinDate(entry);
		this.lastFeedUpdate = extractLastFeedUpdate(entry);
		this.lastWebAccess = extractLastWebAccess(entry, stats);
		this.crawlDate = extractCrawled();
		this.uploadCount = extractUploadCount(entry);
		this.favouritesCount = extractFavouritesCount(entry);
		this.subscriberCount = extractSubscriberCount(entry, stats);
		this.videoWatchCount = extractVideoWatchCount(entry, stats);
		this.totalUploadViews = extractTotalUploadViews(entry, stats);
		this.isMale = extractIsMale(entry);
		this.location = extractLocation(entry);

		this.uploadedVideosLink = extractUploadedVideosLink(entry);
		this.favedVideosLink = extractFavedVideosLink(entry);
		this.subscriptionsLink = extractSubscriptionsLink(entry);
	}


	/**
	 * Getter for the Logger.
	 * 
	 * @return the logger.
	 */
	private Logger log()
	{
		return ExtractedUser.logger;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.entities.ExtractedEntity#accept(ytharvest.factory.entities.
	 * ExtractedEntityVisitor)
	 */
	@Override
	public ExtractedEntity accept(ExtractedEntityVisitor visitor)
	{
		return visitor.visit(this);
	};


	/**
	 * Extracts profile statistics. Helper function allowing to extract these statisctics only
	 * once.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the object representing some statisctics about the user.
	 */
	private YtUserProfileStatistics extractProfileStats(UserProfileEntry entry)
	{
		YtUserProfileStatistics stats = entry.getStatistics();

		if (stats == null)
			log().warn("Null statistics object for user {}", this.getUserName());

		return stats;
	}


	/**
	 * Extracts user's name.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the string representing user's name.
	 * @throws UnretrievableUserNameException
	 *             thrown when user's name could not have been extracted.
	 */
	private String extractUserName(UserProfileEntry entry) throws UnretrievableUserNameException
	{
		String userName = entry.getUsername();
		if (userName != null && !userName.isEmpty())
			return userName;

		log().error("Could not retrieve user's name from UserProfileEntry object!");
		log().error("User's id: {}", entry.getId());
		throw new UnretrievableUserNameException();
	}


	/**
	 * Extracts user's profile url.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the address of this user's profile.
	 */
	private String extractProfileUrl(UserProfileEntry entry)
	{
		// Wyrzucić, zostawić?? Można pobierać przez selfLink().
		String profileUrl = entry.getId();
		if (profileUrl == null || profileUrl.equals(""))
		{
			log().warn("Null profile url for user {}.", this.getUserName());
			return "";
		}

		// Eksperymentalne parsowanie w celu wydobycia userID. Patrz komentarz w
		// klasie YtUser.
		{
			String[] split = profileUrl.split(":");
			boolean parseSuccess = true;

			if (split.length != 4)
				parseSuccess = false;
			else
			{
				String userID = split[3];
				if (userID == null || userID.length() < 10)
					parseSuccess = false;
			}

			if (!parseSuccess)
				log().debug("Parsing user's profile failed for user {}, profile: {}",
						this.getUserName(), profileUrl);
		}

		return profileUrl;
	}


	/**
	 * Extracts the date, when this user joined YouTube.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the user's join date.
	 */
	private Date extractJoinDate(UserProfileEntry entry)
	{
		DateTime joinDate = entry.getPublished();
		if (joinDate == null)
		{
			log().warn("Null join date for user " + this.getUserName());
			return new Date(0);
		}
		return new Date(joinDate.getValue());
	}


	/**
	 * Extracts the date, when feed representing user's data was last updated.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the feed's last update date.
	 */
	private Date extractLastFeedUpdate(UserProfileEntry entry)
	{
		DateTime feedUpd = entry.getUpdated();

		if (feedUpd == null)
		{
			log().warn("Null last feed update date for user {}", this.getUserName());
			return new Date(0);
		}
		return new Date(feedUpd.getValue());
	}


	/**
	 * Extracts the date, when user last accessed their profile via the YouTube web page.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @param stats
	 *            the YtUserProfileStatistics object obtained from an UserProfileEntry.
	 * @return the user's last web access date.
	 */
	private Date extractLastWebAccess(UserProfileEntry entry, YtUserProfileStatistics stats)
	{
		if (stats == null)
			return new Date(0);

		DateTime webAcc = stats.getLastWebAccess();

		if (webAcc == null)
		{
			log().warn("Null last web access date for user {}", this.getUserName());
			return new Date(0);
		}

		return new Date(webAcc.getValue());
	}


	/**
	 * Extracts the number of videos uploaded by this user.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the number of uploaded videos.
	 */
	private Integer extractUploadCount(UserProfileEntry entry)
	{
		FeedLink<?> upl = entry.getUploadsFeedLink();
		if (upl == null)
		{
			log().warn("Null uploads feed link for user {}", this.getUserName());
			return ExtractedUser.retrievalFailMarker;
		}
		return upl.getCountHint();
	}


	/**
	 * Extracts the number of user's favourited videos.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the number of user's favourite videos.
	 */
	private Integer extractFavouritesCount(UserProfileEntry entry)
	{
		FeedLink<?> favs = entry.getFavoritesFeedLink();
		if (favs != null)
		{
			Integer countH = favs.getCountHint();
			if (countH != null)
				return countH;
			else
				log().warn("Null count hint in favourites feedlink for user {}.",
						this.getUserName());
		}
		else
			log().warn("Null favorites link for user {}", this.getUserName());

		return ExtractedUser.retrievalFailMarker;
	}


	/**
	 * Extracts the number of users subscribed to this user's account.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @param stats
	 *            the YtUserProfileStatistics object obtained from an UserProfileEntry.
	 * @return the number of users subscribed to this user's account.
	 */
	private Integer extractSubscriberCount(UserProfileEntry entry, YtUserProfileStatistics stats)
	{
		if (stats != null)
			return (int) stats.getSubscriberCount();

		return ExtractedUser.retrievalFailMarker;
	}


	/**
	 * Extracts the number of videos watched by this user.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @param stats
	 *            the YtUserProfileStatistics object obtained from an UserProfileEntry.
	 * @return the number of videos watched by this user.
	 */
	private Integer extractVideoWatchCount(UserProfileEntry entry, YtUserProfileStatistics stats)
	{
		if (stats != null)
			return (int) stats.getVideoWatchCount();

		return ExtractedUser.retrievalFailMarker;
	}


	/**
	 * Extracts the total number of this user's uploaded videos views.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @param stats
	 *            the YtUserProfileStatistics object obtained from an UserProfileEntry.
	 * @return the number of this user's uploaded videos views.
	 */
	private Integer extractTotalUploadViews(UserProfileEntry entry, YtUserProfileStatistics stats)
	{
		if (stats != null)
			return (int) stats.getTotalUploadViews();

		return ExtractedUser.retrievalFailMarker;
	}


	/**
	 * Extracts the user's gender flag.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return true, if user is a male, false otherwise.
	 */
	private boolean extractIsMale(UserProfileEntry entry)
	{
		YtGender.Value gender = entry.getGender();
		if (gender == null)
		{
			log().warn("Null gender for user {}", this.getUserName());
			return true;
		}
		return gender == YtGender.Value.MALE;
	}


	/**
	 * Extracts user's location.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the string representing user's location.
	 */
	private String extractLocation(UserProfileEntry entry)
	{
		String loc = entry.getLocation();
		if (loc == null || loc.isEmpty())
		{
			log().warn("Null location for user {}", this.getUserName());
			return "";
		}
		return loc;
	}


	/**
	 * Creates the date when this user's profile was crawled.
	 * 
	 * @return the user's profile crawl date.
	 */
	private Date extractCrawled()
	{
		return new Date();
	}


	/**
	 * Extracts the link of this user's uploaded videos feed.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the string representing feed's address.
	 */
	private String extractUploadedVideosLink(UserProfileEntry entry)
	{
		FeedLink<?> upl = entry.getUploadsFeedLink();
		if (upl != null)
		{
			String href = upl.getHref();
			if (href != null && !href.isEmpty())
				return href;
			else
				log().warn("Null uploads link for user {}.", this.getUserName());
		}
		else
			log().warn("Null uploads feed link for user {}", this.getUserName());

		return null;
	}


	/**
	 * Extracts the link of this user's favourite videos feed.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the string representing feed's address.
	 */
	private String extractFavedVideosLink(UserProfileEntry entry)
	{
		FeedLink<?> favs = entry.getFavoritesFeedLink();
		if (favs != null)
		{
			String href = favs.getHref();
			if (href != null && !href.isEmpty())
				return href;
			else
				log().warn("Null favoutires link for user {}.", this.getUserName());
		}
		else
			log().warn("Null favorites feed link for user {}", this.getUserName());

		return null;
	}


	/**
	 * Extracts the link of this user's subscriptions feed.
	 * 
	 * @param entry
	 *            the UserProfileEntry object obtained from YouTubeService.
	 * @return the string representing feed's address.
	 */
	private String extractSubscriptionsLink(UserProfileEntry entry)
	{
		FeedLink<?> subs = entry.getSubscriptionsFeedLink();
		if (subs != null)
		{
			String href = subs.getHref();
			if (href != null && !href.isEmpty())
				return href;
			else
				log().warn("Null subscriptions link for user {}.", this.getUserName());
		}
		else
			log().warn("Null subscriptions feed link for user {}.", this.getUserName());

		return null;
	}


	/**
	 * Gets the user's name.
	 * 
	 * @return the user's name.
	 */
	public String getUserName()
	{
		return userName;
	}


	/**
	 * Gets the user's profile url.
	 * 
	 * @return the user's profile url.
	 */
	public String getProfileUrl()
	{
		return profileUrl;
	}


	/**
	 * Gets the date, when user joined YouTube.
	 * 
	 * @return the date, when user joined YouTube.
	 */
	public Date getJoinDate()
	{
		return joinDate;
	}


	/**
	 * Gets the date when feed representing user's data was last updated.
	 * 
	 * @return the date when feed representing user's data was last updated.
	 */
	public Date getLastFeedUpdate()
	{
		return lastFeedUpdate;
	}


	/**
	 * Gets the date, when user last accessed their profile via the YouTube web page.
	 * 
	 * @return the date, when user last accessed their profile via the YouTube web page.
	 */
	public Date getLastWebAccess()
	{
		return lastWebAccess;
	}


	/**
	 * Gets the date when this user's profile was crawled.
	 * 
	 * @return the date when this user's profile was crawled.
	 */
	public Date getCrawlDate()
	{
		return crawlDate;
	}


	/**
	 * Gets the number of videos uploaded by this user.
	 * 
	 * @return the number of videos uploaded by this user.
	 */
	public int getUploadCount()
	{
		return uploadCount;
	}


	/**
	 * Gets the number of user's favourited videos.
	 * 
	 * @return the number of user's favourited videos.
	 */
	public int getFavouritesCount()
	{
		return favouritesCount;
	}


	/**
	 * Gets the number of users subscribed to this user's account.
	 * 
	 * @return the number of users subscribed to this user's account.
	 */
	public int getSubscriberCount()
	{
		return subscriberCount;
	}


	/**
	 * Gets the number of videos watched by this user.
	 * 
	 * @return the number of videos watched by this user.
	 */
	public int getVideoWatchCount()
	{
		return videoWatchCount;
	}


	/**
	 * Gets the total number of this user's uploaded videos views.
	 * 
	 * @return the total number of this user's uploaded videos views.
	 */
	public int getTotalUploadViews()
	{
		return totalUploadViews;
	}


	/**
	 * Gets the user's gender flag.
	 * 
	 * @return true, if user is a male, false otherwise.
	 */
	public boolean isMale()
	{
		return isMale;
	}


	/**
	 * Gets the user's location.
	 * 
	 * @return the user's location.
	 */
	public String getLocation()
	{
		return location;
	}


	/**
	 * Gets the link of this user's uploaded videos feed.
	 * 
	 * @return the link of this user's uploaded videos feed.
	 */
	public String getUploadedVideosLink()
	{
		return uploadedVideosLink;
	}


	/**
	 * Gets the link of this user's favourite videos feed.
	 * 
	 * @return the link of this user's favourite videos feed.
	 */
	public String getFavedVideosLink()
	{
		return favedVideosLink;
	}


	/**
	 * Gets the link of this user's subscriptions feed.
	 * 
	 * @return the link of this user's subscriptions feed.
	 */
	public String getSubscriptionsLink()
	{
		return subscriptionsLink;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("========================== User: ").append(this.getUserName()).append('\n');
		builder.append("ProfileUrl: ").append(this.getProfileUrl()).append('\n');
		builder.append("Join date: ").append(this.getJoinDate()).append('\n');
		builder.append("Last feed update: ").append(this.getLastFeedUpdate()).append('\n');
		builder.append("Last web access: ").append(this.getLastWebAccess()).append('\n');
		builder.append("Crawl date: ").append(this.getCrawlDate()).append('\n');
		builder.append("Upload count: ").append(this.getUploadCount()).append('\n');
		builder.append("Favorites count: ").append(this.getFavouritesCount()).append('\n');
		builder.append("Subscribers count: ").append(this.getSubscriberCount()).append('\n');
		builder.append("Video watch count: ").append(this.getVideoWatchCount()).append('\n');
		builder.append("Total uploads views: ").append(this.getTotalUploadViews()).append('\n');
		builder.append("Location: ").append(this.getLocation()).append('\n');
		builder.append("Male: ").append(this.isMale()).append('\n');
		builder.append("Uploaded videos link: ").append(this.getUploadedVideosLink()).append('\n');
		builder.append("Faved videos link: ").append(this.getFavedVideosLink()).append('\n');
		builder.append("Subscirptions link: ").append(this.getSubscriptionsLink()).append('\n');
		builder.append("====================================================================");
		return builder.toString();
	}

}

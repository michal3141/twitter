package ytharvest.factory.entities;


import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.exceptions.UnretrievableVideoIdException;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtStatistics;


/**
 * The ExtractedVideo class represents represents information about an YouTube video.
 */
public class ExtractedVideo extends ExtractedEntity
{

	/**
	 * The retrieval fail marker - some fields may have this value indicating that they should be
	 * considered as not having any value. Analogous to 'null' value for references.
	 */
	public static int retrievalFailMarker = -1;

	/** The Logger object. */
	private static Logger logger = LoggerFactory.getLogger(ExtractedVideo.class);

	/** The video's identifier. */
	private String videoID;

	/** The video's title. */
	private String title;

	/** The video's description. */
	private String description;

	/** The category video was assigned to. */
	private String category;

	/** The video's tags. */
	private Collection<String> tags;

	/** The time, when video was published. */
	private Date published;

	/** The time, when video was last updated. */
	private Date updated;

	/** The time, when video was crawled. */
	private Date crawled;

	/** The video's rating. */
	private float rating;

	/** The number of comments posted to this video. */
	private int commentCount;

	/** The number of video's ratings. */
	private int ratingCount;

	/** The number of times that this video was watched. */
	private int watchCount;

	/** The number of times that this video was favourited. */
	private int favouriteCount;

	/** The video uploader's id. */
	private String uploaderId;

	/** The address of a feed containing videos that reply to this video. */
	private String repliesLink;

	/** The address of a feed containing this video's comments. */
	private String commentsLink;


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the Logger object.
	 */
	private Logger log()
	{
		return ExtractedVideo.logger;
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
	 * Instantiates a new ExtractedVideo.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @throws UnretrievableVideoIdException
	 *             thrown when video's id could not have been extracted.
	 */
	public ExtractedVideo(VideoEntry entry) throws UnretrievableVideoIdException
	{
		log().debug("Extracting video entry.");

		YouTubeMediaGroup mediaG = getMediaGroup(entry);

		this.videoID = extractVideoId(entry, mediaG);
		this.title = extractTitle(entry);
		this.description = extractDescription(entry, mediaG);
		this.category = extractCategory(entry, mediaG);
		this.tags = extractTags(entry, mediaG);
		this.published = extractPublished(entry);
		this.updated = extractUpdated(entry);
		this.crawled = extractCrawled();
		this.rating = extractRating(entry);
		this.commentCount = extractCommentCount(entry);
		this.ratingCount = extractRatingCount(entry);
		this.watchCount = extractWatchCount(entry);
		this.favouriteCount = extractFavouriteCount(entry);
		this.uploaderId = extractUploaderId(entry, mediaG);
		this.repliesLink = extractRepliesLink(entry);
		this.commentsLink = extractCommentsLink(entry);

		// Testowe pobieranie różnych innych wartości i pól z obiektu VideoEntry.
		// try
		// {
		// log().debug("VIDEO EXTRACT TEST [{}]:", this.getVideoId());
		// //
		// log().debug("Author URI: {}", entry.getAuthors().get(0).getUri());
		//
		// log().debug("YTMediaCategory, label: {}, content: {}",
		// mediaG.getYouTubeCategory().getLabel(),
		// mediaG.getYouTubeCategory().getContent());
		// log().debug("YTMediaCategory, scheme: {}", mediaG.getYouTubeCategory().getScheme());
		// if (entry.getComplaintsLink() == null)
		// log().debug("Complaints link null.");
		// else
		// log().debug("Complaints link: {}", entry.getComplaintsLink().getHref());
		//
		// log().debug("Location: {}", entry.getLocation());
		//
		// if (entry.getPublicationState() == null)
		// log().debug("Publication state null.");
		// else
		// log().debug("Publication state, description: {}, enum value {}",
		// entry.getPublicationState().getDescription(),
		// entry.getPublicationState().getState().toString());
		//
		// if (entry.getRatingLink() == null)
		// log().debug("Rating link null.");
		// else
		// log().debug("Rating link: {}", entry.getRatingLink().getHref());
		//
		// if (entry.getRecorded() == null)
		// log().debug("Recorded date null.");
		// else
		// log().debug("Recorded date: {}", entry.getRecorded().toString());
		// }
		// catch (Exception e)
		// {
		// log().debug("Exception during VIDEO EXTRACT TEST.", e);
		// }
	}


	/**
	 * Extracts the 'MediaGroup' object. Helper function allowing to extract this object only once.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the MediaGroup object.
	 */
	private YouTubeMediaGroup getMediaGroup(VideoEntry entry)
	{
		YouTubeMediaGroup mediaG = entry.getMediaGroup();
		if (mediaG == null)
			log().warn("Null media group for video {}.", this.getVideoId());

		return mediaG;
	}


	/**
	 * Extracts the video's identifier.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @param media
	 *            the MediaGroup object obtained from a VideoEntry.
	 * @return the video's identifier.
	 * @throws UnretrievableVideoIdException
	 *             thrown when video's id could not have been extracted.
	 */
	private String extractVideoId(VideoEntry entry, YouTubeMediaGroup media)
			throws UnretrievableVideoIdException
	{
		if (media != null)
		{
			String id = media.getVideoId();
			if (id != null && !id.equals(""))
				return id;
		}

		log().error("Could not retrieve video id from VideoEntry object!");
		log().error("Video id: {}.", entry.getId());
		throw new UnretrievableVideoIdException();
	}


	/**
	 * Extracts the video's title.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the video's title.
	 */
	private String extractTitle(VideoEntry entry)
	{

		TextConstruct textC = entry.getTitle();
		if (textC != null)
		{
			String title = textC.getPlainText();
			if (title != null && !title.equals(""))
				return title;
			else
				log().warn("Null title for video {}.", this.getVideoId());
		}
		else
			log().warn("Null title text construct for video {}.", this.getVideoId());

		return "";
	}


	/**
	 * Extracts the video's description.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @param mediaG
	 *            the MediaGroup object obtained from a VideoEntry.
	 * @return the video's description.
	 */
	private String extractDescription(VideoEntry entry, YouTubeMediaGroup mediaG)
	{
		if (mediaG != null)
		{
			MediaDescription mediaD = mediaG.getDescription();
			if (mediaD != null)
			{
				String desc = mediaD.getPlainTextContent();
				if (desc != null && !desc.equals(""))
					return desc;
				else
					log().warn("Null description for video {}.", this.getVideoId());
			}
			else
				log().warn("Null MediaDescriprion for video {}.", this.getVideoId());
		}

		return "";
	}


	/**
	 * Extracts the category video was assigned to.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @param mediaG
	 *            the MediaGroup object obtained from a VideoEntry.
	 * @return the category video was assigned to.
	 */
	private String extractCategory(VideoEntry entry, YouTubeMediaGroup mediaG)
	{
		if (mediaG != null)
		{
			MediaCategory mediaC = mediaG.getYouTubeCategory();
			if (mediaC != null)
			{
				String cat = mediaC.getLabel();
				if (cat != null && !cat.isEmpty())
					return cat;
				else
					log().warn("Empty category string for video {}.", this.getVideoId());
			}
			else
				log().warn("Empty category object for video {}.", this.getVideoId());
		}

		return "";
		// Stare pobieranie kategorii, oparte o funkcję getCategories z klasy BaseFeed. Zostawione
		// na wszelki wypadek.
		// Set<Category> cats = entry.getCategories();
		//
		// if (cats != null && !cats.isEmpty())
		// {
		// for (Category cat : cats)
		// if (cat.getTerm() != null && cat.getLabel() != null)
		// return cat.getTerm();
		//
		// log().warn("No category found for video {}.", this.getVideoId());
		// }
		// else
		// log().warn("Null categories set for video {}.", this.getVideoId());
		//
		// return "";
	}


	/**
	 * Extracts the video's tags.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @param mediaG
	 *            the MediaGroup object obtained from a VideoEntry.
	 * @return the video's tags.
	 */
	private Collection<String> extractTags(VideoEntry entry, YouTubeMediaGroup mediaG)
	{
		if (mediaG != null)
		{
			MediaKeywords words = mediaG.getKeywords();
			if (words != null)
			{
				List<String> tags = words.getKeywords();
				if (tags != null && !tags.isEmpty())
					return tags;
				else
					log().warn("Null tags set for video {}.", this.getVideoId());
			}
			else
				log().warn("No MediaKeywords found for video {}.", this.getVideoId());
		}

		return Collections.emptyList();
	}


	/**
	 * Extracts the time, when video was published.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the time, when video was published.
	 */
	private Date extractPublished(VideoEntry entry)
	{
		DateTime pub = entry.getPublished();
		if (pub != null)
			return new Date(pub.getValue());
		else
			log().warn("Null publication date for video {}.", this.getVideoId());

		return new Date(0);
	}


	/**
	 * Extracts the time, when video was last updated.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the time, when video was last updated.
	 */
	private Date extractUpdated(VideoEntry entry)
	{
		DateTime upd = entry.getUpdated();
		if (upd != null)
			return new Date(upd.getValue());
		else
			log().warn("Null update date for video {}.", this.getVideoId());

		return new Date(0);
	}


	/**
	 * Extracts the time, when video was crawled.
	 * 
	 * @return the time, when video was crawled.
	 */
	private Date extractCrawled()
	{
		return new Date();
	}


	/**
	 * Extracts the video's rating.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the video's rating.
	 */
	private float extractRating(VideoEntry entry)
	{
		Rating r = entry.getRating();

		if (r != null)
			return r.getAverage();
		else
		{
			log().debug("Null rating for video {}.", this.getVideoId());
			return 0;
		}
	}


	/**
	 * Extracts the number of comments posted to this video.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the number of comments posted to this video.
	 */
	private int extractCommentCount(VideoEntry entry)
	{
		Comments coms = entry.getComments();
		if (coms != null)
		{
			FeedLink<?> comLink = coms.getFeedLink();
			if (comLink != null)
				return comLink.getCountHint();
			else
				log().warn("Null comments link (and count) for video {}.", this.getVideoId());
		}
		else
			log().warn("Null comments object (and count) for video {}.", this.getVideoId());

		return ExtractedVideo.retrievalFailMarker;
	}


	/**
	 * Extracts the number of video's ratings.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the number of video's ratings.
	 */
	private int extractRatingCount(VideoEntry entry)
	{

		Rating r = entry.getRating();

		if (r != null)
			return r.getNumRaters();
		else
		{
			log().debug("Null rating (count) for video {}.", this.getVideoId());
			return 0;
		}
	}


	/**
	 * Extracts the number of times that this video was watched.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the number of times that this video was watched.
	 */
	private int extractWatchCount(VideoEntry entry)
	{
		YtStatistics stats = entry.getStatistics();
		if (stats != null)
			return (int) stats.getViewCount();
		else
			log().warn("Null statistics (watch count) for video {}.", this.getVideoId());

		return ExtractedVideo.retrievalFailMarker;
	}


	/**
	 * Extracts the number of times that this video was favourited.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the number of times that this video was favourited.
	 */
	private int extractFavouriteCount(VideoEntry entry)
	{
		YtStatistics stats = entry.getStatistics();
		if (stats != null)
			return (int) stats.getFavoriteCount();
		else
			log().warn("Null statistics (favourite count) for video {}.", this.getVideoId());

		return ExtractedVideo.retrievalFailMarker;
	}


	/**
	 * Extracts the video uploader's id.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @param mediaG
	 *            the MediaGroup object obtained from a VideoEntry.
	 * @return the video uploader's id.
	 */
	private String extractUploaderId(VideoEntry entry, YouTubeMediaGroup mediaG)
	{
		if (mediaG != null)
		{
			String uId = mediaG.getUploader();
			if (uId != null && !uId.isEmpty())
				return uId;
			else
				log().warn("Null uploader id for video {}.", this.getVideoId());
		}

		return "";
	}


	/**
	 * Extracts the address of a feed containing videos that reply to this video.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the address of a feed containing videos that reply to this video.
	 */
	private String extractRepliesLink(VideoEntry entry)
	{
		Link reps = entry.getVideoResponsesLink();
		if (reps != null)
		{
			String link = reps.getHref();
			if (link != null && !link.isEmpty())
				return link;
			else
				log().warn("Null link address for video {}.", this.getVideoId());
		}
		else
			log().warn("Null link for video {}.", this.getVideoId());

		return null;
	}


	/**
	 * Extracts the address of a feed containing this video's comments.
	 * 
	 * @param entry
	 *            the VideoEntry obtained from YouTubeService.
	 * @return the address of a feed containing this video's comments.
	 */
	private String extractCommentsLink(VideoEntry entry)
	{
		Comments coms = entry.getComments();
		if (coms != null)
		{
			FeedLink<?> comLink = coms.getFeedLink();
			if (comLink != null)
			{
				String addr = comLink.getHref();
				if (addr != null && !addr.isEmpty())
					return addr;
				else
					log().warn("Null comments address for video {}.", this.getVideoId());
			}
			else
				log().warn("Null comments link for video {}.", this.getVideoId());
		}
		else
			log().warn("Null comments object for video {}.", this.getVideoId());

		return null;
	}


	/**
	 * Gets the video's identifier.
	 * 
	 * @return the video's identifier.
	 */
	public String getVideoId()
	{
		return this.videoID;
	}


	/**
	 * Gets video's title.
	 * 
	 * @return the video's title.
	 */
	public String getTitle()
	{
		return title;
	}


	/**
	 * Gets the video's description.
	 * 
	 * @return the video's description.
	 */
	public String getDescription()
	{
		return description;
	}


	/**
	 * Gets the category video was assigned to.
	 * 
	 * @return the category video was assigned to.
	 */
	public String getCategory()
	{
		return category;
	}


	/**
	 * Gets the video's tags.
	 * 
	 * @return the video's tags.
	 */
	public Collection<String> getTags()
	{
		return tags;
	}


	/**
	 * Gets the time, when video was published.
	 * 
	 * @return the time, when video was published.
	 */
	public Date getPublished()
	{
		return published;
	}


	/**
	 * Gets the time, when video was last updated.
	 * 
	 * @return the time, when video was last updated.
	 */
	public Date getUpdated()
	{
		return updated;
	}


	/**
	 * Gets the time, when video was crawled.
	 * 
	 * @return the time, when video was crawled.
	 */
	public Date getCrawled()
	{
		return crawled;
	}


	/**
	 * Gets the video's rating.
	 * 
	 * @return the video's rating.
	 */
	public float getRating()
	{
		return rating;
	}


	/**
	 * Gets the number of comments posted to this video.
	 * 
	 * @return the number of comments posted to this video.
	 */
	public int getCommentCount()
	{
		return commentCount;
	}


	/**
	 * Gets the number of video's ratings.
	 * 
	 * @return the number of video's ratings.
	 */
	public int getRatingCount()
	{
		return ratingCount;
	}


	/**
	 * Gets the number of times that this video was watched.
	 * 
	 * @return the number of times that this video was watched.
	 */
	public int getWatchCount()
	{
		return watchCount;
	}


	/**
	 * Gets the number of times that this video was favourited.
	 * 
	 * @return the number of times that this video was favourited.
	 */
	public int getFavouriteCount()
	{
		return favouriteCount;
	}


	/**
	 * Gets the video uploader's id.
	 * 
	 * @return the video uploader's id.
	 */
	public String getUploaderId()
	{
		return uploaderId;
	}


	/**
	 * Gets the replies link.
	 * 
	 * @return the replies link
	 */
	public String getRepliesLink()
	{
		return repliesLink;
	}


	/**
	 * Gets the address of a feed containing this video's comments.
	 * 
	 * @return the address of a feed containing this video's comments.
	 */
	public String getCommentsLink()
	{
		return commentsLink;
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
		builder.append("========================== Video ").append(this.getVideoId()).append('\n');
		builder.append("Title: ").append(this.getTitle()).append('\n');
		builder.append("Description: ").append(this.getDescription()).append("\n");
		builder.append("Category: ").append(this.getCategory()).append("\n");
		builder.append("Tags: ").append(this.getTags()).append("\n");
		builder.append("Published: ").append(this.getPublished()).append("\n");
		builder.append("Updated: ").append(this.getUpdated()).append("\n");
		builder.append("Crawled: ").append(this.getCrawled()).append('\n');
		builder.append("Rating: ").append(this.getRating()).append('\n');
		builder.append("Comment count: ").append(this.getCommentCount()).append('\n');
		builder.append("Rating count: ").append(this.getRatingCount()).append('\n');
		builder.append("Watch count: ").append(this.getWatchCount()).append('\n');
		builder.append("Favourite count: ").append(this.getFavouriteCount()).append('\n');
		builder.append("Uploader's id: ").append(this.getUploaderId()).append('\n');
		builder.append("Replies link: ").append(this.getRepliesLink()).append('\n');
		builder.append("Comments link: ").append(this.getCommentsLink()).append('\n');
		builder.append("=========================================================================");
		return builder.toString();
	}
}

package ytharvest.factory.entities;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.exceptions.UnretrievableCommentIdException;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.youtube.CommentEntry;
import com.google.gdata.data.youtube.YouTubeNamespace;


/**
 * The ExtractedComment class represents information about one comment posted to YouTube website.
 */
public class ExtractedComment extends ExtractedEntity
{

	/** The Logger object. */
	protected final static Logger log = LoggerFactory.getLogger(ExtractedComment.class);

	/** The id of the comment. */
	private String id;

	/** The comment's title, usually first few words of a comment. */
	private String title;

	/** The actual content. */
	private String content;

	/** Publication date. */
	private Date published;

	/** Last update date. */
	private Date updated;

	// private String videoId;
	// private String authorId;

	/** The author's name. */
	private String authorName;

	/** Spam mark - true if comment was marked as spam. */
	private boolean spamMark;

	/** The address of a comment, which this comment is a reply to. */
	protected String repliesToLink = null;

	/** The comment's rating. */
	protected Integer rating = null;


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
	}


	/**
	 * Instantiates a new ExtractedComment. Performs the data extraction.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @throws UnretrievableCommentIdException
	 *             thrown when comment's id could not have been extracted.
	 */
	public ExtractedComment(CommentEntry entry) throws UnretrievableCommentIdException
	{
		log().debug("Extracting comment entry.");

		this.id = extractId(entry);
		this.title = extractTitle(entry);
		this.content = extractContent(entry);
		this.published = extractPublished(entry);
		this.updated = extractUpdated(entry);
		this.authorName = extractAuthorName(entry);
		this.spamMark = extractSpamMark(entry);
		this.repliesToLink = extractRepliesToLink(entry);
		this.rating = extractRating(entry);

		// Testowe pobieranie różnych innych wartości i pól z obiektu CommentEntry.
		// try
		// {
		// log().debug("COMMENT EXTRACT TEST [{}]:", this.getId());
		// log().debug("Total rating: {}", entry.getTotalRating());
		// }
		// catch (Exception e)
		// {
		// log().debug("Exception during COMMENT EXTRACT TEST.", e);
		// }
	}


	/**
	 * Extracts rating.
	 * 
	 * @param p_entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the integer representing comment's rating.
	 */
	protected Integer extractRating(CommentEntry p_entry)
	{
		Integer rating = p_entry.getTotalRating();
		if (rating == null)
		{
			log.warn("Null rating for comment: " + getId());
			return 0;
		}
		return rating;
	}


	/**
	 * Extracts link to replied comment.
	 * 
	 * @param p_entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the address of a comment being replied.
	 */
	protected String extractRepliesToLink(CommentEntry p_entry)
	{

		List<Link> links = p_entry.getLinks();
		if (links == null)
		{
			log().warn("Null links for comment: " + getId());
		}
		for (Link link : links)
		{
			if (YouTubeNamespace.IN_REPLY_TO.equals(link.getRel())) { return link.getHref(); }
		}
		return null;
	}


	/**
	 * Getter for the logger object.
	 * 
	 * @return the logger
	 */
	private Logger log()
	{
		return ExtractedComment.log;
	}


	/**
	 * Extracts the comment's id.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the string representing comment's identification number.
	 * @throws UnretrievableCommentIdException
	 *             thrown when comment's id could not have been extracted.
	 */
	private String extractId(CommentEntry entry) throws UnretrievableCommentIdException
	{
		// Czy potrzebne? Czy na pewno nie dzielić??
		String id = entry.getId();
		if (id != null && !id.isEmpty())
		{
			// return id;
			String[] split = id.split(":");
			if (split.length >= 6)
				return split[5];
			else
			{
				log().error("ID split tab shorter than 6 for comment: {}", id);
				return null;
			}
		}
		else
		{
			log().error("Null comment id!");
			throw new UnretrievableCommentIdException();
		}
	}


	/**
	 * Extracts comment's title.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the string representing comment's title.
	 */
	private String extractTitle(CommentEntry entry)
	{
		TextConstruct titleConstr = entry.getTitle();
		if (titleConstr != null)
		{
			String title = titleConstr.getPlainText();
			if (title != null && !title.isEmpty())
				return title;
			else
				log().warn("Null title for comment {}", this.getId());
		}
		else
			log().warn("Null title construct for comment {}", this.getId());

		return "";
	}


	/**
	 * Extracts comment's content.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the string representing comment's content.
	 */
	private String extractContent(CommentEntry entry)
	{
		String content = entry.getPlainTextContent();
		if (content != null && !content.isEmpty())
			return content;

		log().warn("Null content for comment {}.", this.getId());
		return "";
	}


	/**
	 * Extracts comment's publication date.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the date when this comment was published.
	 */
	private Date extractPublished(CommentEntry entry)
	{
		DateTime pub = entry.getPublished();
		if (pub != null)
			return new Date(pub.getValue());
		else
			log().warn("Null publication date for comment {}.", this.getId());

		return new Date(0);
	}


	/**
	 * Extracts comment's last update date.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the date when this comment was last updated.
	 */
	private Date extractUpdated(CommentEntry entry)
	{
		DateTime upd = entry.getUpdated();
		if (upd != null)
			return new Date(upd.getValue());
		else
			log().warn("Null update date for comment {}.", this.getId());

		return new Date(0);
	}


	// private String extractVideoId(CommentEntry entry)
	// {
	// String id = entry.getId();
	// String[] split = id.split(":");
	// return split[3];
	// }

	// private String extractAuthorId(CommentEntry entry)
	// {
	// List<Person> authors = entry.getAuthors();
	// if(authors.size() == 1)
	// {
	// String userId = authors.get(0).
	// }
	//
	// }

	/**
	 * Extracts comment's author's name.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return the string representing author's name.
	 */
	private String extractAuthorName(CommentEntry entry)
	{
		List<Person> authors = entry.getAuthors();
		if (authors.size() > 0)
		{
			if (authors.size() > 1)
				log().warn("Authors list for comment {} longer than 1, choosing first author.",
						this.getId());

			String authorName = authors.get(0).getName();
			if (authorName != null && !authorName.isEmpty())
				return authorName;
			else
				log().warn("Null author username for comment {}.", this.getId());
		}
		else
			log().warn("Null authors list for comment {}", this.getId());

		return "";
	}


	/**
	 * Extracts the comment's spam mark.
	 * 
	 * @param entry
	 *            the CommentEntry object, obtained from YouTubeService.
	 * @return true, if this comment was marked as spam, false otherwise.
	 */
	private boolean extractSpamMark(CommentEntry entry)
	{
		return entry.hasSpamHint();
	}


	/**
	 * Gets the comment's id.
	 * 
	 * @return the comment's id.
	 */
	public String getId()
	{
		return this.id;
	}


	/**
	 * Gets the comment's title.
	 * 
	 * @return the comment's title.
	 */
	public String getTitle()
	{

		return this.title;
	}


	/**
	 * Gets the comment's content.
	 * 
	 * @return the comment's content.
	 */
	public String getContent()
	{
		return content;
	}


	/**
	 * Gets the comment's publication date.
	 * 
	 * @return the comment's publication date.
	 */
	public Date getPublished()
	{
		return published;
	}


	/**
	 * Gets the comment's last update date.
	 * 
	 * @return the comment's last update date.
	 */
	public Date getUpdated()
	{
		return updated;
	}


	/**
	 * Gets the comment's author's name.
	 * 
	 * @return the comment's author's name.
	 */
	public String getAuthorName()
	{
		return authorName;
	}


	/**
	 * Gets the comment's spam mark.
	 * 
	 * @return the comment's spam mark.
	 */
	public boolean getSpamMark()
	{
		return spamMark;
	}


	/**
	 * Gets the link to the replied comment.
	 * 
	 * @return the link to the replied comment.
	 */
	public String getRepliesToLink()
	{
		return this.repliesToLink;
	}


	/**
	 * Gets the comment's rating.
	 * 
	 * @return the comment's rating.
	 */
	public Integer getRating()
	{
		return this.rating;
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
		builder.append("======================== Comment ").append(getId()).append('\n');
		builder.append("Title: ").append(getTitle()).append('\n');
		builder.append("Content: ").append(getContent()).append('\n');
		builder.append("Published ").append(getPublished()).append('\n');
		builder.append("Updated ").append(getUpdated()).append('\n');
		builder.append("Author: ").append(getAuthorName()).append('\n');
		builder.append("Spam mark: ").append(getSpamMark()).append('\n');
		builder.append("===========================================================================\n");
		return builder.toString();
	}

}

package ytharvest.factory.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The VideoFilter class is a helper class intended to ease implementation of video entries filters.
 * It implements three methods of {@link FeedFilter} , namely
 * {@link FeedFilter#visit(ExtractedEntity)}, {@link FeedFilter#visit(ExtractedUser)} and
 * {@link FeedFilter#visit(ExtractedComment)}. As this filter is supposed to filter only
 * {@link ytharvest.factory.entities.ExtractedVideo} objects, aforementioned methods log errors when
 * some other {@link ytharvest.factory.entities.ExtractedEntity} is received by VideoFilter. When
 * subclassing VideoFilter, only {@link FeedFilter#visit(ExtractedVideo)}, {@link FeedFilter#more()}
 * and {@link FeedFilter#copy()} need to provided.<br>
 * <br>
 * 
 * Pass VideoFilter as an appropriate argument to a
 * {@link FilterFactory#configure(FeedFilter, FeedFilter, FeedFilter)} method to make sure that it
 * is used for filtering videos only.
 * 
 * @see FeedFilter
 */
public abstract class VideoFilter extends FeedFilter
{

	/** The Logger object. */
	private static Logger log = LoggerFactory.getLogger(VideoFilter.class);


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger.
	 */
	private Logger log()
	{
		return VideoFilter.log;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedEntity)
	 */
	@Override
	public ExtractedEntity visit(ExtractedEntity entity)
	{
		log().error("Received unknown ExtractedEntity:\n" + entity.toString());
		reject(entity);
		return entity;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedUser)
	 */
	@Override
	public ExtractedUser visit(ExtractedUser user)
	{
		log().error("Received user:\n" + user.toString());
		reject(user);
		return user;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedComment)
	 */
	@Override
	public ExtractedComment visit(ExtractedComment comment)
	{
		log().error("Received comment:\n" + comment.toString());
		reject(comment);
		return comment;
	}

}

package ytharvest.factory.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The UserFilter class is a helper class intended to ease implementation of user entries filters.
 * It implements three methods of {@link FeedFilter} , namely
 * {@link FeedFilter#visit(ExtractedEntity)}, {@link FeedFilter#visit(ExtractedVideo)} and
 * {@link FeedFilter#visit(ExtractedComment)}. As this filter is supposed to filter only
 * {@link ytharvest.factory.entities.ExtractedUser} objects, aforementioned methods log errors when
 * some other {@link ytharvest.factory.entities.ExtractedEntity} is received by UserFilter. When
 * subclassing UserFilter, only {@link FeedFilter#visit(ExtractedUser)}, {@link FeedFilter#more()}
 * and {@link FeedFilter#copy()} need to provided. <br>
 * <br>
 * 
 * Pass UserFilter as an appropriate argument to a
 * {@link FilterFactory#configure(FeedFilter, FeedFilter, FeedFilter)} method to make sure that it
 * is used for filtering users only.
 * 
 * @see FeedFilter
 */
public abstract class UserFilter extends FeedFilter
{

	/** The Logger object. */
	private static Logger log = LoggerFactory.getLogger(UserFilter.class);


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger.
	 */
	private Logger log()
	{
		return UserFilter.log;
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
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedVideo)
	 */
	@Override
	public ExtractedVideo visit(ExtractedVideo video)
	{
		log().error("Received video:\n" + video.toString());
		reject(video);
		return video;
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

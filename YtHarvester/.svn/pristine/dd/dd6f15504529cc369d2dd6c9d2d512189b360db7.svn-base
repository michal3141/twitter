package ytharvest.factory.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The CommentFilter class is a helper class intended to ease implementation of comment entries
 * filters. It implements three methods of {@link FeedFilter} , namely
 * {@link FeedFilter#visit(ExtractedEntity)}, {@link FeedFilter#visit(ExtractedUser)} and
 * {@link FeedFilter#visit(ExtractedVideo)}. As this filter is supposed to filter only
 * {@link ytharvest.factory.entities.ExtractedComment} objects, aforementioned methods log errors when
 * some other {@link ytharvest.factory.entities.ExtractedEntity} is received by CommentFilter. When
 * subclassing CommentFilter, only {@link FeedFilter#visit(ExtractedComment)}, {@link FeedFilter#more()}
 * and {@link FeedFilter#copy()} need to provided.<br>
 * <br>
 * 
 * Pass CommentFilter as an appropriate argument to a
 * {@link FilterFactory#configure(FeedFilter, FeedFilter, FeedFilter)} method to make sure that it
 * is used for filtering comments only.
 * 
 * @see FeedFilter
 */
public abstract class CommentFilter extends FeedFilter
{

	/** The Logger object. */
	private static Logger log = LoggerFactory.getLogger(CommentFilter.class);


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger.
	 */
	private Logger log()
	{
		return CommentFilter.log;
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
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedVideo)
	 */
	@Override
	public ExtractedVideo visit(ExtractedVideo video)
	{
		log().error("Received video:\n" + video.toString());
		reject(video);
		return video;
	}

}

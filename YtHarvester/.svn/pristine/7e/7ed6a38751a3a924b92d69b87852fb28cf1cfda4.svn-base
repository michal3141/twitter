package ytharvest.factory.filters;


import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The NullFilter class implements an empty filter, that is the filter that accepts everything.
 * 
 * @see FeedFilter
 */
public class NullFilter extends FeedFilter
{


	/* (non-Javadoc)
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedEntity)
	 */
	@Override
	public ExtractedEntity visit(ExtractedEntity entity)
	{
		accept(entity);
		return entity;
	}



	/* (non-Javadoc)
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedUser)
	 */
	@Override
	public ExtractedUser visit(ExtractedUser user)
	{
		accept(user);
		return user;
	}



	/* (non-Javadoc)
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedVideo)
	 */
	@Override
	public ExtractedVideo visit(ExtractedVideo video)
	{
		accept(video);
		return video;
	}


	/* (non-Javadoc)
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedComment)
	 */
	@Override
	public ExtractedComment visit(ExtractedComment comment)
	{
		accept(comment);
		return comment;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#more()
	 */
	@Override
	public boolean more()
	{
		return true;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#copy()
	 */
	@Override
	public FeedFilter copy()
	{
		return new NullFilter();
	}

}

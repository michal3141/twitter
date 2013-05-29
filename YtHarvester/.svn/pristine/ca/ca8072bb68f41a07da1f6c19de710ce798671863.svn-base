package ytharvest.factory.filters;


import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The QuantityFilter implements filter that limits the quantity of accepted entities to a given
 * maximum number. When this maximum is reached, {@link #more()} function will return false.
 * 
 * @see FeedFilter
 */
public class QuantityFilter extends FeedFilter
{

	/** The max number of accepted entities. */
	private int max;


	/**
	 * Instantiates a new quantity filter.
	 * 
	 * @param max
	 *            the maximum number of accepted entities.
	 */
	public QuantityFilter(int max)
	{
		this.max = max;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#visit(ytharvest.factory.entities.ExtractedEntity)
	 */
	@Override
	public ExtractedEntity visit(ExtractedEntity entity)
	{
		accept(entity);
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
		accept(user);
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
		accept(video);
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
		return getAcceptedEntities().size() < max;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.filters.FeedFilter#copy()
	 */
	@Override
	public FeedFilter copy()
	{
		return new QuantityFilter(max);
	}

}

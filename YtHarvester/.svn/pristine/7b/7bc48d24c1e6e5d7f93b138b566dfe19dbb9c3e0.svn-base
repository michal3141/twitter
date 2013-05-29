package ytharvest.factory.filters;


import java.util.Collection;
import java.util.LinkedList;

import ytharvest.factory.entities.ExtractedComment;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedEntityVisitor;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;


/**
 * The FeedFilter is a base class for all entities filters. <br>
 * <br>
 * 
 * Filtering is used in {@link ytharvest.factory.dispatchers.FeedDispatcher} to limit the amount of
 * entitites in downloaded feeds. This number (especially in case of comments) can get overwhelming.
 * Filters serve to save the bandwidth and speed up the crawling process. <br>
 * <br>
 * 
 * FeedFilter is an abstract class providing some convenience mechanisms (namely accepting and
 * rejecting entitities) to help in implementing other filters, and an interface that every filter
 * must implement ({@link #more()} and {@link #copy()} functions as well as all of the functions
 * from {@link ytharvest.factory.entities.ExtractedEntityVisitor} interface). <br>
 * <br>
 * 
 * Filter must implement all of the functions from
 * {@link ytharvest.factory.entities.ExtractedEntityVisitor} interface. Even if filter is supposed
 * to be used only for {@link ytharvest.factory.entities.ExtractedUser} objects (for instance), it
 * still must have functions accepting other {@link ytharvest.factory.entities.ExtractedEntity
 * ExtractedEntities} as arguments, but may reject them. Convenience classes are provided:
 * {@link UserFilter}, {@link VideoFilter}, {@link CommentFilter}. {@link FilterFactory} should be
 * properly configured to create filters for appropriate feed types. <br>
 * <br>
 * 
 * Note: each filter is supposed to be used only once, for one feed. Then the filter is destroyed
 * and another one is created for another feed. {@link FilterFactory} uses the {@link #copy()}
 * function to create new filters out of existing ones.
 * 
 * @see FilterFactory
 */
public abstract class FeedFilter implements ExtractedEntityVisitor
{

	/** The collection containing accepted entities. See {@link #accept(ExtractedEntity)}. */
	private Collection<ExtractedEntity> accepts = new LinkedList<>();

	/** The collection containing rejected entities. See {@link #reject(ExtractedEntity)}. */
	private Collection<ExtractedEntity> rejects = new LinkedList<>();


	/**
	 * Function indicating if more feed pages should be downloaded. Some filters may know in the
	 * middle of feed processing that no more entities will be accepted. This function then can tell
	 * {@link ytharvest.factory.dispatchers.FeedDispatcher} to stop the downloading of current feed.
	 * 
	 * @return true, if more entities can be accepted (and therefore should be downloaded), false if
	 *         the download process should stop.
	 */
	public abstract boolean more();


	/**
	 * Function for copying filter. Each filter is supposed to be used to filter one feed, then the
	 * filter is destroyed. {@link FilterFactory} is responsible for creating new filters, and it
	 * does so by copying filters it has been configured with (or default ones).
	 * 
	 * @return the copy of this filter.
	 */
	public abstract FeedFilter copy();


	/**
	 * Function to check given {@link ytharvest.factory.entities.ExtractedEntity} for acceptance.
	 * Called whenever {@link #doFilter(ExtractedEntity)} was called with
	 * {@link ytharvest.factory.entities.ExtractedEntity} as an argument. <br>
	 * <br>
	 * 
	 * Note that this particular function will not be normally called, as there are no YouTube
	 * entities that would be represented direclty as
	 * {@link ytharvest.factory.entities.ExtractedEntity} objects - only its subclasses are used.
	 * 
	 * @see ytharvest.factory.entities.ExtractedEntityVisitor#visit(ytharvest.factory.entities.ExtractedEntity)
	 */
	@Override
	public abstract ExtractedEntity visit(ExtractedEntity entity);


	/**
	 * Function to check given {@link ytharvest.factory.entities.ExtractedUser} for acceptance.
	 * Called whenever {@link #doFilter(ExtractedEntity)} was called with
	 * {@link ytharvest.factory.entities.ExtractedUser} as an argument.
	 * 
	 * @see ytharvest.factory.entities.ExtractedEntityVisitor#visit(ytharvest.factory.entities.ExtractedUser)
	 */
	@Override
	public abstract ExtractedUser visit(ExtractedUser user);


	/**
	 * Function to check given {@link ytharvest.factory.entities.ExtractedComment} for acceptance.
	 * Called whenever {@link #doFilter(ExtractedEntity)} was called with
	 * {@link ytharvest.factory.entities.ExtractedComment} as an argument.
	 * 
	 * @see ytharvest.factory.entities.ExtractedEntityVisitor#visit(ytharvest.factory.entities.ExtractedComment)
	 */
	@Override
	public abstract ExtractedComment visit(ExtractedComment comment);


	/**
	 * Function to check given {@link ytharvest.factory.entities.ExtractedVideo} for acceptance.
	 * Called whenever {@link #doFilter(ExtractedEntity)} was called with
	 * {@link ytharvest.factory.entities.ExtractedVideo} as an argument.
	 * 
	 * @see ytharvest.factory.entities.ExtractedEntityVisitor#visit(ytharvest.factory.entities.ExtractedComment)
	 */
	@Override
	public abstract ExtractedVideo visit(ExtractedVideo video);


	/**
	 * Helper function to ease implementing new filters. Subclasses may call it to store accepted
	 * entities in internal collection, for later retrieval with {@link #getAcceptedEntities()}.
	 * 
	 * @param entity
	 *            the entity to accept.
	 */
	protected void accept(ExtractedEntity entity)
	{
		accepts.add(entity);
	}


	/**
	 * Helper function to ease implementing new filters. Subclasses may call it to store rejected
	 * entities in internal collection, for later retrieval with {@link #getRejectedEntities()}.
	 * 
	 * @param e
	 *            the entity to reject.
	 */
	protected void reject(ExtractedEntity e)
	{
		rejects.add(e);
	}


	/**
	 * Gets the accepted entities.
	 * 
	 * @return the accepted entities.
	 */
	public Collection<ExtractedEntity> getAcceptedEntities()
	{
		return accepts;
	}


	/**
	 * Gets the rejected entities.
	 * 
	 * @return the rejected entities.
	 */
	public Collection<ExtractedEntity> getRejectedEntities()
	{
		return rejects;
	}


	/**
	 * Function performing actual filtering. Uses the Visitor design pattern to delegate filtering
	 * to one of the functions from {@link ytharvest.factory.entities.ExtractedEntityVisitor}
	 * interface.
	 * 
	 * @param entity
	 *            the entity to check for acceptation or rejection.
	 */
	public void doFilter(ExtractedEntity entity)
	{
		entity.accept(this);
	}
}

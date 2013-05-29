package ytharvest.factory.stats;


import ytharvest.factory.entities.ExtractedEntityVisitor;


/**
 * The Gatherer is an interface of objects listening for information about downloaded entities. <br>
 * <br>
 * Whenever {@link StatisticsFacade} downloads a new entity, it passes that entity to all registered
 * {@link Gatherer Gatherers}. They may perform any action, commonly logging some information about
 * an entity (its type, time of download, etc.). This allows {@link Gatherer Gatherers} to provide
 * some statistics about a download process. <br>
 * <br>
 * Methods {@link #feedStart()} and {@link #feedEnd()} mark beginning and end of a feed. All
 * entities passed to a {@link Gatherer} between calls to these two methods belong to one feed.
 * 
 * @see StatisticsFacade
 */
public interface Gatherer extends ExtractedEntityVisitor
{

	/**
	 * Marks the start of a feed. All entities passed to this {@link Gatherer} after this function
	 * and before {@link #feedEnd()} belong to one feed.
	 */
	public void feedStart();


	/**
	 * Marks the end of a feed. All entities passed to this {@link Gatherer} after
	 * {@link #feedStart()} and before this function belong to one feed.
	 */
	public void feedEnd();
}

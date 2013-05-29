package ytharvest.factory.stats;


import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import ytharvest.factory.EntryFactoryFacade;
import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedUser;
import ytharvest.factory.entities.ExtractedVideo;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;
import ytharvest.factory.exceptions.UnretrievableUserNameException;
import ytharvest.factory.exceptions.UnretrievableVideoIdException;


/**
 * The {@link StatisticsFacade} is a version of {@link EntryFactoryFacade} that allows for easy gathering of
 * statistical information about downloaded entities. It provides a mechanism of adding 'gatherers',
 * objects implementing interface {@link Gatherer}, which receive information about processed feeds
 * and entities. {@link Gatherer Gatherers} may then perform virtually any action - logging some
 * information is an example.<br>
 * <br>
 * 
 * {@link StatisticsFacade} is derived from {@link EntryFactoryFacade}, so it can be used wherever that
 * class is used.<br>
 * <br>
 * 
 * {@link StatisticsFacade} uses functions from {@link EntryFactoryFacade} to download actual
 * objects. Whenever an object is downloaded, this class passes it to all registered gatherers.
 * Whenever a new feed is downloaded, gatherers are informed about start of a new feed, are given
 * the entries of a feed, and then are informed about end of the feed. See {@link Gatherer} for
 * details.
 * 
 * @see EntryFactoryFacade
 * @see Gatherer
 */
public class StatisticsFacade extends EntryFactoryFacade
{

	/** The collection of {@link Gatherer} objects. */
	private Collection<Gatherer> gaths = new LinkedList<>();


	/**
	 * Instantiates a new StatisticsFacade - delegates to {@link EntryFactoryFacade} constructor.
	 */
	public StatisticsFacade()
	{
		super();
	}


	/**
	 * Registers a new {@link Gatherer}.
	 * 
	 * @param g
	 *            the {@link Gatherer} to add.
	 */
	public void addGatherer(Gatherer g)
	{
		gaths.add(g);
	}


	/**
	 * Passes given {@link ExtractedEntity} to all registered {@link Gatherer Gatherers}.
	 * 
	 * @param e
	 *            the entity to gather.
	 */
	private void gather(ExtractedEntity e)
	{
		for (Gatherer g : gaths)
			e.accept(g);
	}


	/**
	 * Passes given collection of {@link ExtractedEntity ExtractedEntities} to all registered
	 * {@link Gatherer Gatherers}.
	 * 
	 * @param c
	 *            the collection of entities to gather.
	 */
	private void gather(Collection<? extends ExtractedEntity> c)
	{
		for (Gatherer g : gaths)
			g.feedStart();

		for (ExtractedEntity e : c)
			gather(e);

		for (Gatherer g : gaths)
			g.feedEnd();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.EntryFactoryFacade#getUserEntry(java.lang.String)
	 */
	@Override
	public ExtractedUser getUserEntry(String ID) throws UnretrievableUserNameException,
			HarvServiceForbiddenException, HarvServiceException, IOException
	{
		ExtractedUser u = super.getUserEntry(ID);

		gather(u);

		return u;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.EntryFactoryFacade#getVideoEntry(java.lang.String)
	 */
	@Override
	public ExtractedVideo getVideoEntry(String ID) throws UnretrievableVideoIdException,
			HarvServiceForbiddenException, HarvServiceException, IOException
	{
		ExtractedVideo v = super.getVideoEntry(ID);

		gather(v);

		return v;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.EntryFactoryFacade#getUserFeed(java.lang.String)
	 */
	@Override
	public Collection<ExtractedEntity> getUserFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		Collection<ExtractedEntity> c = super.getUserFeed(URL);

		gather(c);

		return c;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.EntryFactoryFacade#getVideoFeed(java.lang.String)
	 */
	@Override
	public Collection<ExtractedEntity> getVideoFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		Collection<ExtractedEntity> c = super.getVideoFeed(URL);

		gather(c);

		return c;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ytharvest.factory.EntryFactoryFacade#getCommentFeed(java.lang.String)
	 */
	@Override
	public Collection<ExtractedEntity> getCommentFeed(String URL)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		Collection<ExtractedEntity> c = super.getCommentFeed(URL);

		gather(c);

		return c;
	}

}

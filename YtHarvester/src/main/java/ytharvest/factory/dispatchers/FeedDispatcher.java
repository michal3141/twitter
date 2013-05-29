package ytharvest.factory.dispatchers;


import java.io.IOException;
import java.util.Collection;

import ytharvest.factory.entities.ExtractedEntity;
import ytharvest.factory.entities.ExtractedFeed;
import ytharvest.factory.exceptions.FeedExtractionException;
import ytharvest.factory.exceptions.HarvServiceException;
import ytharvest.factory.exceptions.HarvServiceForbiddenException;
import ytharvest.factory.filters.FeedFilter;


/**
 * Abstract class implementing algortihm for iterative downloading of pages in an entities feed. The
 * {@link #dispatch(String, FeedFilter)} method contains a generic method, while subclasses are
 * responsible for obtaining actual pages.
 */
public abstract class FeedDispatcher
{

	/**
	 * The generic downloading method. It is an iterative algorithm that downloads conscutive pages,
	 * filters their entries and returns results accepted by filter.
	 * 
	 * @param URL
	 *            address of a feed containing desired entities.
	 * @param filter
	 *            the filter used to limit the size of a result.
	 * @return the collection of accepted entries of a feed.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access entries in a given feed.
	 * @throws FeedExtractionException
	 *             thrown when at least one of entries' data extractions throws an
	 *             {@link ytharvest.factory.exceptions.EntryExtractionException}. Such errors may be
	 *             thrown during construction of {@link ytharvest.factory.entities.ExtractedEntity
	 *             ExtractedEntities'} subclasses.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Collection<ExtractedEntity> dispatch(String URL, FeedFilter filter)
			throws HarvServiceForbiddenException, FeedExtractionException, HarvServiceException,
			IOException
	{
		// Collection<ExtractedEntity> entries = Collections.emptyList();
		// FeedFilter filter = getFilter();

		String pageUrl = URL;
		while (pageUrl != null && filter.more())
		{
			ExtractedFeed page = getFeedPage(pageUrl);

			for (ExtractedEntity entry : page.getEntities())
				filter.doFilter(entry);

			// entries.addAll(/* filter.doFilter( */page.getEntries()/* ) */);

			pageUrl = page.getNextFeedLink();
		}

		return filter.getAcceptedEntities();
	}


	/**
	 * Abstract method for obtaining one page in a feed, it is to be implemented in subclasses (the
	 * 'Template Method' pattern).
	 * 
	 * @param url
	 *            address of a page to download.
	 * @return the page represented as an ExtractedFeed object.
	 * @throws HarvServiceForbiddenException
	 *             thrown when GData throws ServiceForbiddenException, indicating that you do not
	 *             have rights to access entries in a given feed.
	 * @throws FeedExtractionException
	 *             thrown when at least one of entries' data extractions throws an
	 *             {@link ytharvest.factory.exceptions.EntryExtractionException}. Such errors may be
	 *             thrown during construction of {@link ytharvest.factory.entities.ExtractedEntity
	 *             ExtractedEntities'} subclasses.
	 * @throws HarvServiceException
	 *             some other GData error occured.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected abstract ExtractedFeed getFeedPage(String url) throws HarvServiceForbiddenException,
			FeedExtractionException, HarvServiceException, IOException;
}

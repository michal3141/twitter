package ytharvest.factory.filters;


/**
 * A factory for creating Filter objects. This class provides static functions for creating new
 * filters. User can provide default filters which will be copied on every request for a new filter. <br>
 * <br>
 * 
 * This class is mainly used by {@link ytharvest.factory.EntryFactoryFacade} and
 * {@link ytharvest.factory.dispatchers.FeedDispatcher}. The former instantiates filters using
 * FilterFactory and passes them to the
 * {@link ytharvest.factory.dispatchers.FeedDispatcher#dispatch(String, FeedFilter)} method.
 * 
 * @see FeedFilter
 */
public class FilterFactory
{
	// private static FeedFilter filter = new NullFilter();

	/** The filter used for filtering user feeds. */
	private static FeedFilter userF = getDefaultFilter();

	/** The filter used for filtering video feeds. */
	private static FeedFilter vidF = getDefaultFilter();

	/** The filter used for filtering comment feeds. */
	private static FeedFilter commF = getDefaultFilter();


	/**
	 * Main configure function. User must provide user, video and comment filters which will be used
	 * for consequent filter requests.
	 * 
	 * @param u
	 *            the user filter.
	 * @param v
	 *            the video filter.
	 * @param c
	 *            the comment filter.
	 */
	public static void configure(FeedFilter u, FeedFilter v, FeedFilter c)
	{
		userF = u;
		vidF = v;
		commF = c;
	}


	/**
	 * Creates new default filter.
	 * 
	 * @return the default filter.
	 */
	public static FeedFilter getDefaultFilter()
	{
		return new NullFilter();
	}


	/**
	 * Creates new user filter.
	 * 
	 * @return the user filter.
	 */
	public static FeedFilter getUserFilter()
	{
		return userF.copy();
	}


	/**
	 * Creates new video filter.
	 * 
	 * @return the video filter.
	 */
	public static FeedFilter getVideoFilter()
	{
		return vidF.copy();
	}


	/**
	 * Creates new comment filter.
	 * 
	 * @return the comment filter.
	 */
	public static FeedFilter getCommentFilter()
	{
		return commF.copy();
	}
}

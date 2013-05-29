package ytharvest.factory;


import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ytharvest.properties.Literals;
import ytharvest.properties.Properties;


/**
 * Helper class for manipulating url addresses.
 */
public class URLCreator
{

	/** The Logger object. */
	private static Logger log = LoggerFactory.getLogger(URLCreator.class);

	// http://gdata.youtube.com/feeds/api/videos/VIDEO_ID
	// http://gdata.youtube.com/feeds/api/users/USER_ID

	/** The base user url. */
	private static String userAddr = Properties.get(Literals.USERURL_PREFIX);

	/** The base video url. */
	private static String vidAddr = Properties.get(Literals.VIDURL_PREFIX);


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger
	 */
	private static Logger log()
	{
		return log;
	}


	/**
	 * Returns given string address as an URL object.
	 * 
	 * @param url
	 *            String object representing an address.
	 * @return the url as an URL object.
	 */
	public static URL getUrl(String url)
	{
		try
		{
			return new URL(url);
		}
		catch (MalformedURLException e)
		{
			log().error("Caught MalformedURLException (check .properties file?)! Bad url: " + url,
					e);
		}
		return null;
	}


	/**
	 * Prefixes given user ID with user base url.
	 * 
	 * @param id
	 *            user's id.
	 * @return the address of given user's profile.
	 */
	public static String getUserAddress(String id)
	{
		return userAddr + id;
	}


	/**
	 * Prefixes given video ID with video base url.
	 * 
	 * @param id
	 *            ID of a video.
	 * @return the address of a given video.
	 */
	public static String getVideoAddress(String id)
	{
		return vidAddr + id;
	}
}

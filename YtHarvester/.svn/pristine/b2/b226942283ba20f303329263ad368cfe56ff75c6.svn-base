package ytharvest.properties;


import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class containing settings needed by YtHarvester to function properly. <br>
 * <br>
 * These Properties need to be configured in order to create an
 * {@link ytharvest.factory.EntryFactoryFacade} object. <br>
 * <br>
 * Properties is a wrapper for standard Java {@link java.util.Properties} class. It is impossible to
 * instantiate it, but it needs to be configured by calling one of the methods:
 * {@link #configure(String)}, {@link #configure()}, {@link #configure(java.util.Properties)}.
 * Provided .properties file or map needs to contain all of the {@link Literals} members as keys. <br>
 * <br>
 * The elements of {@link Literals} are used as arguments to {@link #get(Literals)} function. Thanks
 * to that, user does not need to pass String arguments to
 * {@link java.util.Properties#getProperty(String)} function, which is very uncomfortable and
 * error-prone (what if a key needs to be changed? What if there is a typo in a provided String?).
 * {@link Literals} hide the String keys in themselves, allowing users to use {@link Literals}
 * values.
 * 
 * @see Literals
 */
public class Properties
{

	/** The default .properties file path - used when no path is supplied. */
	protected static String DEFAULT_PROPERTIES_PATH = "target/classes/harvest.properties";

	/** The standard Java {@link java.util.Properties} - the actual map of key-value pairs. */
	protected static java.util.Properties properties;

	/** The Logger object. */
	protected static Logger log = LoggerFactory.getLogger(Properties.class);


	/**
	 * Getter for the Logger object.
	 * 
	 * @return the logger object.
	 */
	private static Logger log()
	{
		return Properties.log;
	}


	/**
	 * Function reading the settings from default file. Delegates to {@link #configure(String)}.
	 */
	public static void configure()
	{
		Properties.configure(DEFAULT_PROPERTIES_PATH);
	}


	/**
	 * Function reading the settings from a given file. It initializes internal
	 * {@link java.util.Properties} object with the contents of a file defined by its path. It
	 * requires the file to have a standard Java .properties file format.
	 * 
	 * @param path
	 *            a path to .properties file.
	 */
	public static void configure(String path)
	{
		log().info("Initializing harvester properties from file: {}.", path);

		try
		{
			properties = new java.util.Properties();
			properties.load(new FileReader(path));
			log().info("Initialization succesfull.");
		}
		catch (Exception e)
		{
			log().error("Properties file couldn't have been loaded: {}.", path);
			log().error("Check above path and restart application.", e);
		}
	}


	/**
	 * Initialization of setting by providing existing {@link java.util.Properties} object. Instead
	 * of reading the map from a file ({@link #configure(String)}), this function expects a map of
	 * settings in the form of {@link java.util.Properties} instance. It allows for programmatical
	 * creation and modification of library's settings.
	 * 
	 * @param jProperties
	 *            the standard Java {@link java.util.Properties} map containing all of the required
	 *            settings.
	 */
	public static void configure(java.util.Properties jProperties)
	{
		log().info("Initializing harvester properties programatically.");
		properties = jProperties;
	}


	/**
	 * Gets the desired property. {@link Literals} argument is used to obtain a String key, which is
	 * passed to internal {@link java.util.Properties}.
	 * 
	 * @param literal
	 *            the literal value representing desired property.
	 * @return the string being the property's value.
	 */
	public static String get(Literals literal)
	{
		if (properties == null)
			configure(DEFAULT_PROPERTIES_PATH);

		return properties.getProperty(literal.getVal());
	}


	/**
	 * Instantiates a new Properties. Private, because only static methods of Properties class are
	 * meant to be used.
	 */
	private Properties()
	{}

}

package ytharvest.properties;


/**
 * The Literals is a helper enum type for {@link Properties} class.
 * 
 * The values of this type are supposed to be used as arguments of the
 * {@link Properties#get(Literals)} method. That way, one can omit the traditional and error-prone
 * way of supplying properties keys as String arguments (which is used in the standard Java
 * {@link java.util.Properties} class), and instead use easily manageable enum values. Each such
 * value has a String value contained inside, whish is used by {@link Properties} class to supply
 * key to a standard Java {@link java.util.Properties}. <br>
 * <br>
 * When user wants to configure the {@link Properties} class by providing their own map of key-value
 * settings, that map must contain all of the Literals string values as keys (plus their
 * corresponding values).
 * 
 * @see Properties
 */
public enum Literals
{

	// //////////////////////////////////////////
	// String Literals

	/** The harvester application name - the name supplied to a YouTubeService object. */
	HARVESTER_APPNAME("YtServiceName"), /**
	 * The develeoper key - key supplied to a YouTubeService
	 * object.
	 */
	DEVELEOPER_KEY("DeveloperKey"), /**
	 * The prefix to which video ID should be appended in order to
	 * receive video's url.
	 */
	VIDURL_PREFIX("VideoUrlPrefix"), /**
	 * The prefix to which user ID should be appended in order
	 * to receive user's url.
	 */
	USERURL_PREFIX("UserUrlPrefix");

	// End of string literals
	// //////////////////////////////////////////

	/** The string val. */
	private String stringVal;


	/**
	 * Instantiates a new literals object. Requires String value that will be used as a key for
	 * obraining actual property value.<br>
	 * <br>
	 * Note, that it is private constructor, disallowing creation of Literals variables. They are
	 * only meant to be used direct values.
	 * 
	 * @param stringVal
	 *            the string value contained in every Literals value.
	 */
	private Literals(String stringVal)
	{
		this.stringVal = stringVal;
	}


	/**
	 * Getter for the String value.
	 * 
	 * @return the String value of given Literal.
	 */
	public String getVal()
	{
		return this.stringVal;
	}


	/**
	 * Helper function to print all of the values.
	 */
	static public void printValues()
	{
		for (Literals l : Literals.values())
			System.out.println("Literal: " + l.toString() + ", string value:" + l.getVal());
	}
}

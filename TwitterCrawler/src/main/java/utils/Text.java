package utils;

public class Text {
	private static final int LEN = 63;
	/**
	 * 
	 * @return processed text of tweet
	 */
	public static String processText(String text) {
		text = (text.length() > LEN) ? text.substring(0, LEN) : text;
		text = text.replaceAll("'", "");
		return text;
	}
}

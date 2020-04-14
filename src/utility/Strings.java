package utility;

/**
 * a Strings concrete class encapsulating some reusable string utility functions.
 * 
 * @author Bachar Rima
 * @see String
 */
public class Strings {
	
	/**
	 * decapitalizes a string.
	 * @param str the string to decapitalize.
	 * @return a new str String with the first letter in lowercase.
	 */
	public static String decapitalize(String str) {
		return (str.charAt(0) + "").toLowerCase() + str.substring(1); 
	}
	
	/**
	 * capitalizes a string.
	 * @param str the string to capitalize.
	 * @return a new str String with the first letter in uppercase.
	 */
	public static String capitalize(String str) {
		return (str.charAt(0) + "").toUpperCase() + str.substring(1);
	}
}

package ytharvest.factory.exceptions;


/**
 * Exception indicating an error of user's identifier (username) extraction. It is assumed, that if
 * such an basic information as entity's identifier could not have been obtained, all of an object
 * is incorrect and should be discarded. Thrown by a constructor
 * {@link ytharvest.factory.entities.ExtractedUser#ExtractedUser(com.google.gdata.data.youtube.UserProfileEntry)}
 * .
 */
public class UnretrievableUserNameException extends EntryExtractionException
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

}

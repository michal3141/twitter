package ytharvest.factory.exceptions;



/**
 * Exception representing an data extraction error during creation of {@link ytharvest.factory.entities.ExtractedUser},
 * {@link ytharvest.factory.entities.ExtractedVideo} and {@link ytharvest.factory.entities.ExtractedComment} objects. Constructors
 * of these classes throw exceptions derived from this class. 
 * 
 * @see ytharvest.factory.entities.ExtractedUser
 * @see ytharvest.factory.entities.ExtractedVideo
 * @see ytharvest.factory.entities.ExtractedComment
 */
public class EntryExtractionException extends ExtractionException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new EntryExtractionException.
	 */
	public EntryExtractionException()
	{
		super();
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public EntryExtractionException(Exception cause)
	{
		super(cause);
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's message and cause.
	 * 
	 * @param message
	 *            the exception's message.
	 * @param cause
	 *            the exception's cause.
	 */
	public EntryExtractionException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's message.
	 * 
	 * @param msg
	 *            the exception's message.
	 */
	public EntryExtractionException(String msg)
	{
		super(msg);
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public EntryExtractionException(Throwable cause)
	{
		super(cause);
	}

}

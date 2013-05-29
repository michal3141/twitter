package ytharvest.factory.exceptions;


/**
 * Exception representing data extraction error (that is, an error that occured during processing of
 * the data returned by GData). Used only to catch exceptions, because only its subclasses (
 * {@link EntryExtractionException} and {@link FeedExtractionException}) are instantiated and thrown.
 * 
 * @see EntryExtractionException
 * @see FeedExtractionException
 * 
 */
public class ExtractionException extends HarvestException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new EntryExtractionException.
	 */
	public ExtractionException()
	{
		super();
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public ExtractionException(Exception cause)
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
	public ExtractionException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's message.
	 * 
	 * @param msg
	 *            the exception's message.
	 */
	public ExtractionException(String msg)
	{
		super(msg);
	}


	/**
	 * Instantiates a new EntryExtractionException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public ExtractionException(Throwable cause)
	{
		super(cause);
	}

}

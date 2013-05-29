package ytharvest.factory.exceptions;


/**
 * Main exception wrapping errors returned by the GData library, i.e. its base exception
 * ServiceException. ServiceException that caused this HarvServiceException to be thrown will be
 * passed as a constructor's 'cause' argument.
 */
public class HarvServiceException extends HarvestException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new HarvServiceException.
	 */
	public HarvServiceException()
	{}


	/**
	 * Instantiates a new HarvServiceException, initializing exception's message.
	 * 
	 * @param msg
	 *            the exception's message.
	 */
	public HarvServiceException(String msg)
	{
		super(msg);
	}


	/**
	 * Instantiates a new HarvServiceException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Instantiates a new HarvServiceException, initializing exception's message and cause.
	 * 
	 * @param message
	 *            the exception's message.
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new HarvServiceException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceException(Exception cause)
	{
		super(cause);
	}

}

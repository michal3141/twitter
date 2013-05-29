package ytharvest.factory.exceptions;


/**
 * Exception representing data access error: one tried to access an entity, having no privileges to
 * do so. It is a wrapper for ServiceForbiddenException from the GData library.
 */
public class HarvServiceForbiddenException extends HarvServiceException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new HarvServiceForbiddenException.
	 */
	public HarvServiceForbiddenException()
	{}


	/**
	 * Instantiates a new HarvServiceForbiddenException, initializing exception's message.
	 * 
	 * @param msg
	 *            the exception's message.
	 */
	public HarvServiceForbiddenException(String msg)
	{
		super(msg);
	}


	/**
	 * Instantiates a new HarvServiceForbiddenException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceForbiddenException(Throwable cause)
	{
		super(cause);
	}


	/**
	 * Instantiates a new HarvServiceForbiddenException, initializing exception's message and cause.
	 * 
	 * @param message
	 *            the exception's message.
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceForbiddenException(String message, Throwable cause)
	{
		super(message, cause);
	}


	/**
	 * Instantiates a new HarvServiceForbiddenException, initializing exception's cause.
	 * 
	 * @param cause
	 *            the exception's cause.
	 */
	public HarvServiceForbiddenException(Exception cause)
	{
		super(cause);
	}

}

package ytharvest.factory.entities;


/**
 * ExtractedEntity is a base class for all of the classes representing YouTube entities, in the form
 * of 'extracted' data, i.e. data already obtained from objects returned by the server. In other
 * words, every 'extracted' object (subclass of ExtractedEntity) is a simple POJO object containing
 * downloaded data.
 * <br><br>
 * 
 * This class is an interface implementing the Visitor design pattern, which allows for elegant
 * access to actual type of an object pointed by an ExtractedEntity reference.
 * 
 * @see ExtractedEntityVisitor
 * @see ExtractedUser
 * @see ExtractedVideo
 * @see ExtractedComment
 */
public class ExtractedEntity
{

	/**
	 * Accepts the visitor object, that is calls its
	 * {@link ExtractedEntityVisitor#visit(ExtractedEntity)} method. Visitors must implement an
	 * {@link ExtractedEntityVisitor} interface. Note that subclasses of ExtractedEntity override
	 * this method (to provide their type).
	 * 
	 * @param visitor
	 *            the visitor object.
	 * @return the extracted entity returned by the visitor.
	 */
	public ExtractedEntity accept(ExtractedEntityVisitor visitor)
	{
		return visitor.visit(this);
	}
}

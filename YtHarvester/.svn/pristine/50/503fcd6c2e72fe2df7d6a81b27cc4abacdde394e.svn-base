package ytharvest.factory.entities;


/**
 * The Interface ExtractedEntityVisitor is a generic interface for all visitors of
 * {@link ExtractedEntity} hierarchy. It is part of a Visitor design patter. Used throughout
 * YtHarvester to elegantly access actual type of an object held by reference to its base class,
 * {@link ExtractedEntity}. Every {@link ExtractedEntity} accepts this visitor, and upon calling
 * {@link ExtractedEntity#accept(ExtractedEntityVisitor)}, visitor will receive this ExtractedEntity
 * as concrete type, one of: {@link ExtractedUser}, {@link ExtractedVideo} or
 * {@link ExtractedComment}, i.e. appropriate visit() method will be called.
 * 
 * @see ExtractedEntity
 * @see ExtractedUser
 * @see ExtractedVideo
 * @see ExtractedComment
 */
public interface ExtractedEntityVisitor
{

	/**
	 * Visit method overloaded for {@link ExtractedEntity} class. Note, that this usually should not
	 * be called, because only instances of classes derived from {@link ExtractedEntity} are
	 * instantiated.
	 * 
	 * @param entity
	 *            the entity to visit.
	 * @return the visited entity.
	 */
	public ExtractedEntity visit(ExtractedEntity entity);


	/**
	 * Visit method overloaded for {@link ExtractedUser}. If
	 * {@link ExtractedEntity#accept(ExtractedEntityVisitor)} was called on an object whose real
	 * type was {@link ExtractedUser} with this visitor as an argument, this method will be called
	 * with that object as argument.
	 * 
	 * @param user
	 *            the user to visit.
	 * @return the visited user.
	 */
	public ExtractedUser visit(ExtractedUser user);


	/**
	 * Visit method overloaded for {@link ExtractedVideo}. If
	 * {@link ExtractedEntity#accept(ExtractedEntityVisitor)} was called on an object whose real
	 * type was {@link ExtractedVideo} with this visitor as an argument, this method will be called
	 * with that object as argument.
	 * 
	 * @param video
	 *            the video to visit.
	 * @return the visited video.
	 */
	public ExtractedVideo visit(ExtractedVideo video);


	/**
	 * Visit method overloaded for {@link ExtractedComment}. If
	 * {@link ExtractedEntity#accept(ExtractedEntityVisitor)} was called on an object whose real
	 * type was {@link ExtractedComment} with this visitor as an argument, this method will be called
	 * with that object as argument.
	 * 
	 * @param comment
	 *            the comment to visit.
	 * @return the visited comment.
	 */
	public ExtractedComment visit(ExtractedComment comment);
}

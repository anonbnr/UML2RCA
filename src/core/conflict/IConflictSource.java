package core.conflict;

import java.util.List;

/**
 * an IConflictSource generic interface that defines a conflict source entity.<br><br> 
 * 
 * A conflict candidate entity that satisfies a conflict predicate is considered to be a conflict source entity
 * If a conflict arises, then it is caused by a conflict source entity. A conflict source entity, 
 * for example, can designate a class belonging to a conflict scope, itself defined by a set of classes 
 * within a conflict domain entity. The conflict domain entity could be a metamodeling transformation 
 * class whose execution might give rise to a conflict, when a target class end up owning two identical 
 * attributes, one of which belongs to the conflict source entity.<br><br>
 * 
 * The underlying cause for a conflict is manifested by a set of conflicting elements that are 
 * associated to the originating conflict source entity. Both original elements (i.e. pre-transformation) 
 * and target elements (i.e. post-transformation) are provided to indicate the cause of the conflict,
 * and are used to complete the implementation of a conflict resolution strategy.<br><br>
 * 
 * It must be implemented by any concrete class designating a conflict source.
 *  
 * @author Bachar Rima
 * @see AbstractConflictSource
 *
 * @param <T> The type of the conflict source entity (the same type 
 * of the entities that define the conflict scope for this conflict source entity).
 * @param <E> The type of entities that underly the conflict arising by this conflict source entity
 */
public interface IConflictSource<T, E> extends IConflictCandidate<T, E> {
	
	/* METHODS */
	/**
	 * Returns the list of this conflict source's pre-transformation original elements
	 * @return the list of this conflict source's pre-transformation original elements
	 */
	List<E> getPreTransformationConflictingElements();
	
	/**
	 * Adds a pre-transformation original element to this conflict source's list of 
	 * pre-transformation original elements
	 * @param element the element to add to this conflict source's list of 
	 * pre-transformation original elements
	 * @return true if the element was added, false otherwise 
	 */
	boolean addPreTransformationConflictingElement(E element);

	/**
	 * Returns the list of this conflict source's post-transformation target elements
	 * @return the list of this conflict source's post-transformation target elements
	 */
	List<E> getPostTransformationConflictingElements();
	
	/**
	 * Adds a post-transformation target element to this conflict source's list of 
	 * post-transformation target elements
	 * @param element the element to add to this conflict source's list of 
	 * post-transformation target elements
	 * @return true if the element was added, false otherwise 
	 */
	boolean addPostTransformationConflictingElement(E element);
}

package core.conflict;

import java.util.ArrayList;
import java.util.List;

/**
 * an AbstractConflictSource abstract class that is used to factor 
 * the common interface and state of all concrete conflict source classes.<br><br>
 * 
 * It must be specialized by all concrete conflict source classes.
 * 
 * @author Bachar Rima
 *
 * @param <T> The type of the conflict source entity (the same type 
 * of the entities that define the conflict scope for this conflict source entity).
 * @param <E> The type of entities that underly the conflict arising by this conflict source entity
 */
public abstract class AbstractConflictSource<T, E> extends AbstractConflictCandidate<T, E> 
	implements IConflictSource<T, E> {

	/* ATTRIBUTES */
	/**
	 * The conflict source's list of pre-transformation original elements
	 */
	protected List<E> preTransformationConflictingElements;
	
	/**
	 * The conflict source's list of post-transformation target elements
	 */
	protected List<E> postTransformationConflictingElements;
	
	/* CONSTRUCTOR */
	/**
	 * Creates a conflict source having entity as its underlying entity and conflictScope
	 * as its associated conflict scope. It also initializes its lists of pre/post-transformation
	 * elements as empty array lists.
	 * @param conflictScope a conflict scope to define the conflict scope of this conflict source
	 * @param entity an entity to be used as this conflict source's underlying entity
	 */
	public AbstractConflictSource(AbstractConflictScope<T, E> conflictScope, T entity) {
		super(conflictScope, entity);
		preTransformationConflictingElements = new ArrayList<>();
		postTransformationConflictingElements = new ArrayList<>();
	}

	/* METHODS */
	@Override
	public List<E> getPreTransformationConflictingElements() {
		return preTransformationConflictingElements;
	}
	
	@Override
	public boolean addPreTransformationConflictingElement(E element) {
		return preTransformationConflictingElements.add(element);
	}

	@Override
	public List<E> getPostTransformationConflictingElements() {
		return postTransformationConflictingElements;
	}
	
	@Override
	public boolean addPostTransformationConflictingElement(E element) {
		return postTransformationConflictingElements.add(element);
	}
	
	/**
	 * Always returns true, since a conflict source always satisfies a
	 * conflict predicate with respect to its conflict scope
	 */
	@Override
	public boolean satisfiesConflictCondition() {
		return true;
	}
}

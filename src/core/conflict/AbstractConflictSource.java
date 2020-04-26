package core.conflict;

import java.util.ArrayList;
import java.util.List;

/**
 * an AbstractConflictSource abstract class that defines a conflict source entity.<br><br>
 * 
 * A conflict candidate entity that satisfies a conflict predicate is considered to be a conflict source entity
 * If a conflict arises, then it is caused by a conflict source entity. A conflict source entity, 
 * for example, can designate a class belonging to a conflict scope, itself defined by a set of classes 
 * within a conflict domain entity. The conflict domain entity could be a metamodeling transformation 
 * class whose execution might give rise to a conflict, when a target class end up owning two identical 
 * attributes, one of which belongs to the conflict source entity.<br><br>
 * 
 * The underlying cause for a conflict is manifested by a set of conflicting elements that are 
 * associated to the originating conflict source entity. Both original entities (i.e. pre-transformation) 
 * and target entities (i.e. post-transformation) are provided to indicate the cause of the conflict,
 * and are used to implement a conflict resolution strategy.<br><br>
 *
 * It must be specialized by all concrete conflict source classes.
 * 
 * @author Bachar.RIMA
 *
 * @param <T> The type of the conflict source entity.
 * @param <E> The type of entities that underly the conflict arising by this conflict source entity.
 */
public abstract class AbstractConflictSource<T, E> implements IConflictCandidate<T> {

	/* ATTRIBUTES */
	protected T source;
	protected List<E> preTransformationConflictingElements;
	protected List<E> postTransformationConflictingElements;
	
	/* CONSTRUCTOR */
	public AbstractConflictSource(T source) {
		this.source = source;
		preTransformationConflictingElements = new ArrayList<>();
		postTransformationConflictingElements = new ArrayList<>();
	}

	/* METHODS */
	public T getSource() {
		return source;
	}
	
	public void setSource(T source) {
		this.source = source;
	}
	
	public List<E> getPreTransformationConflictingElements() {
		return preTransformationConflictingElements;
	}
	
	public boolean addPreTransformationConflictingElement(E element) {
		return preTransformationConflictingElements.add(element);
	}

	public List<E> getPostTransformationConflictingElements() {
		return postTransformationConflictingElements;
	}
	
	public boolean addPostTransformationConflictingElement(E element) {
		return postTransformationConflictingElements.add(element);
	}
	
	@Override
	public T getCandidate() {
		return getSource();
	}

	@Override
	public void setCandidate(T candidate) {
		setSource(candidate);
	}
	
	@Override
	public boolean satisfiesConflictCondition() {
		return true;
	}
}

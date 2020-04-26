package core.conflict;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConflictSource<T, E> implements IConflictCandidate<T, E> {

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

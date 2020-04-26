package core.conflict;

public abstract class AbstractConflictCandidate<T, E> implements IConflictCandidate<T, E> {

	/* ATTRIBUTES */
	protected AbstractConflictScope<T, E> conflictScope;
	protected T candidate;
	
	/* CONSTRUCTORS */
	public AbstractConflictCandidate(AbstractConflictScope<T, E> conflictScope, T candidate) {
		this.conflictScope = conflictScope;
		this.candidate = candidate;
	}

	/* METHODS */
	public AbstractConflictScope<T, E> getConflictScope() {
		return this.conflictScope;
	}
	
	@Override
	public T getCandidate() {
		return candidate;
	}

	@Override
	public void setCandidate(T candidate) {
		this.candidate = candidate;
	}
}

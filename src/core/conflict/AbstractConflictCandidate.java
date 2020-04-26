package core.conflict;

/**
 * an AbstractConflictCandidate generic abstract class that is used to factor 
 * the common interface and state of all concrete conflict candidate classes.<br/><br/>
 * 
 * It must be specialized by all concrete conflict candidate classes.
 * 
 * @author Bachar Rima
 * @see IConflictCandidate
 *
 * @param <T> The type of the conflict candidate entity (the same type 
 * of the entities that define the conflict scope for this conflict candidate entity).
 * @param <E> The type of entities that underly the conflict arising by the conflict source entity
 * of the conflict scope for this conflict candidate entity.
 */
public abstract class AbstractConflictCandidate<T, E> implements IConflictCandidate<T> {

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

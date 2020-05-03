package core.conflict;

/**
 * an AbstractConflictCandidate generic abstract class that is used to factor 
 * the common interface and state of all concrete conflict candidate classes.<br><br>
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
public abstract class AbstractConflictCandidate<T, E> implements IConflictCandidate<T, E> {

	/* ATTRIBUTES */
	/**
	 * The conflict scope of this conflict candidate
	 */
	protected AbstractConflictScope<T, E> conflictScope;
	
	/**
	 * The underlying entity of this conflict candidate
	 */
	protected T entity;
	
	/* CONSTRUCTORS */
	/**
	 * Creates an conflict candidate having entity as its underlying entity and conflictScope
	 * as its associated conflict scope
	 * @param conflictScope a conflict scope to define the conflict scope of this conflict candidate
	 * @param entity an entity to be used as this conflict candidate's underlying entity
	 */
	public AbstractConflictCandidate(AbstractConflictScope<T, E> conflictScope, T entity) {
		this.conflictScope = conflictScope;
		this.entity = entity;
	}

	/* METHODS */
	@Override
	public T getEntity() {
		return entity;
	}

	@Override
	public void setEntity(T entity) {
		this.entity = entity;
	}
	
	@Override
	public AbstractConflictScope<T, E> getConflictScope() {
		return this.conflictScope;
	}
	
	@Override
	public void setConflictScope(AbstractConflictScope<T, E> conflictScope) {
		this.conflictScope = conflictScope;
	}
}

package core.conflict;

public abstract class AbstractConflictResolutionStrategy<T, E> {
	/* ATTRIBUTES */
	protected T target;
	protected AbstractConflictScope<T, E> conflictScope;
	protected boolean sameSemantics;
	
	/* CONSTRUCTORS */
	public AbstractConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope, boolean sameSemantics) {
		this.target = target;
		this.conflictScope = conflictScope;
		this.sameSemantics = sameSemantics;
	}
	
	/* METHODS */
	protected abstract void resolve();
}

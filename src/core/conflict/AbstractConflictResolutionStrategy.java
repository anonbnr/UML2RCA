package core.conflict;

/**
 * an AbstractConflictResolutionStrategy generic abstract class that is used to factor 
 * the common interface and state of all concrete conflict resolution strategy classes.<br><br>
 * 
 * It must be specialized by all concrete conflict resolution strategy classes.
 *  
 * @author Bachar Rima
 * @see AbstractConflictScope
 * @see AbstractConflictSource
 *
 * @param <T> The type of entities that define the conflict scope of this conflict resolution strategy.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * resolved by this conflict resolution strategy.
 */
public abstract class AbstractConflictResolutionStrategy<T, E> implements IConflictResolutionStrategy<T, E> {
	
	/* ATTRIBUTES */
	/**
	 * This conflict resolution strategy's target
	 */
	protected T target;
	
	/**
	 * This conflict resolution strategy's conflict scope
	 */
	protected AbstractConflictScope<T, E> conflictScope;
	
	/**
	 * The value that specifies whether the pre/post transformation elements of 
	 * the conflict source of this conflict resolution strategy's conflict scope 
	 * share the same semantics
	 */
	protected boolean sameSemantics;
	
	/* CONSTRUCTORS */
	/**
	 * Creates a conflict resolution strategy having target as its target, 
	 * conflictScope as its conflict scope and sameSemantics to indicate whether 
	 * the pre/post transformation elements of the conflict source of its conflict scope
	 * share the same semantics 
	 * @param target an entity defining the target of this conflict resolution strategy
	 * @param conflictScope a conflict scope defining the conflict scope of this conflict resolution strategy
	 * @param sameSemantics a value specifying whether the pre/post transformation elements of 
	 * the conflict source of this conflict resolution strategy's conflict scope 
	 * share the same semantics
	 */
	public AbstractConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope, 
			boolean sameSemantics) {
		
		this.target = target;
		this.conflictScope = conflictScope;
		this.sameSemantics = sameSemantics;
	}
	
	/* METHODS */
	@Override
	public T getTarget() {
		return target;
	}
	
	@Override
	public void setTarget(T target) {
		this.target = target;
	}
	
	@Override
	public AbstractConflictScope<T, E> getConflictScope() {
		return conflictScope;
	}
	
	@Override
	public void setConflictScope(AbstractConflictScope<T, E> conflictScope) {
		this.conflictScope = conflictScope;
	}
	
	@Override
	public boolean haveSameSemantics() {
		return sameSemantics;
	}
	
	@Override
	public void setSameSemantics(boolean sameSemantics) {
		this.sameSemantics = sameSemantics;
	}
}

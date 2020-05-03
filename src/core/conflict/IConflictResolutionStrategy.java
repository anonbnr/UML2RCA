package core.conflict;

/**
 * an IConflictResolutionStrategy generic interface that defines a conflict resolution strategy.<br><br>
 * 
 * When a conflict is originated by a conflict source entity for a given conflict scope, 
 * it has to be resolved by a conflict resolution strategy. The resolution yields a target entity,
 * having the same type as that of the conflict source entity (and of the entities forming its enclosing
 * conflict scope by extension), that no longer is in a conflict state. 
 * Since a conflict might be resolved using different conflict resolution strategies, 
 * the choice of a conflict resolution strategy is dictated by whether the underlying 
 * conflicting entities share the same semantics or not.<br><br>
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
public interface IConflictResolutionStrategy<T, E> {
	
	/* METHODS */
	/**
	 * Returns this conflict resolution strategy's target entity
	 * @return this conflict resolution strategy's target entity
	 */
	T getTarget();
	
	/**
	 * Sets this conflict resolution strategy's target entity
	 * @param target the value to set this conflict resolution strategy's target entity
	 */
	void setTarget(T target);
	
	/**
	 * Returns this conflict resolution strategy's conflict scope
	 * @return this conflict resolution strategy's conflict scope
	 */
	AbstractConflictScope<T, E> getConflictScope();
	
	/**
	 * Sets this conflict resolution strategy's conflict scope
	 * @param conflictScope the value to set this conflict resolution strategy's conflict scope
	 */
	void setConflictScope(AbstractConflictScope<T, E> conflictScope);
	
	/**
	 * Checks whether the pre/post transformation elements of the conflict source of this 
	 * conflict resolution strategy's conflict scope share the same semantics
	 * @return true if the pre/post transformation elements of the conflict source of this 
	 * conflict resolution strategy's conflict scope share the same semantics, false otherwise
	 */
	boolean haveSameSemantics();
	
	/**
	 * Sets whether the pre/post transformation elements of the conflict source of this 
	 * conflict resolution strategy's conflict scope share the same semantics
	 * @param sameSemantics the value to set whether the pre/post transformation elements 
	 * of the conflict source of this conflict resolution strategy's conflict scope 
	 * share the same semantics
	 */
	void setSameSemantics(boolean sameSemantics);
	
	/**
	 * Defines how this conflict resolution strategy resolves the conflict arising
	 * from the conflict source of its conflict scope, and how it consequently
	 * constructs its returned target
	 */
	void resolve();
}

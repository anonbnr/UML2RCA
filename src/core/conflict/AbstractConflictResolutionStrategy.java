package core.conflict;

/**
 * an AbstractConflictResolutionStrategy generic abstract class that defines a conflict resolution strategy.<br><br>
 * 
 * When a conflict is originated by a conflict source entity for a given conflict scope, 
 * it has to be resolved by a conflict resolution strategy. The resolution yields a target entity,
 * having the same type of the conflict source entity (and of the entities forming its enclosing
 * conflict scope by extension), that no longer is in a conflict state. 
 * Since a conflict might be resolved using different conflict resolution strategies, 
 * the choice of a conflict resolution strategy is dictated by whether the underlying 
 * conflicting entities share the same semantics or not.<br><br>
 * 
 * It must be specialized by all concrete conflict resolution strategy classes.
 *  
 * @author Bachar.RIMA
 * @see AbstractConflictScope
 * @see AbstractConflictSource
 *
 * @param <T> The type of entities that define the conflict scope of this conflict resolution strategy.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * resolved by this conflict resolution strategy.
 */
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

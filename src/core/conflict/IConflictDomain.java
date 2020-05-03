package core.conflict;

/**
 * an IConflictDomain generic interface that defines a domain entity where conflicts might possibly arise
 * during an operation execution. A conflict domain, for example, can designate a metamodeling transformation 
 * whose execution might give rise to a conflict, when a target entity end up owning two identical elements
 * (e.g. having the same name).<br><br> 
 * 
 * This interface provides operations for accessing the conflict domain's active conflict scope,
 * active conflict candidate, active conflict strategy, and active conflict strategy type.<br><br>
 * 
 * It must be implemented by any concrete class designating a conflict domain.
 *  
 * @author Bachar Rima
 * @see AbstractConflictScope
 * @see IConflictCandidate
 * @see AbstractConflictSource
 * @see AbstractConflictResolutionStrategy
 * @see IConflictResolutionStrategyType
 *
 * @param <T> The type of entities that define the conflict scope of a conflict domain.
 * @param <E> The type of entities that underly the conflict arising by a conflict source entity.
 */
public interface IConflictDomain<T, E> {
	
	/* METHODS */
	/**
	 * Returns this conflict domain's active conflict scope
	 * @return this conflict domain's active conflict scope
	 */
	AbstractConflictScope<T, E> getConflictScope();
	
	/**
	 * Returns this conflict domain's active conflict candidate
	 * @return this conflict domain's active conflict candidate
	 */
	AbstractConflictCandidate<T, E> getConflictCandidate();
	
	/**
	 * Return this conflict domain's active conflict resolution strategy
	 * @return this conflict domain's active conflict resolution strategy
	 */
	AbstractConflictResolutionStrategy<T, E> getConflictStrategy();
	
	/**
	 * Return this conflict domain's active conflict resolution strategy type
	 * @return this conflict domain's active conflict resolution strategy type
	 */
	IConflictResolutionStrategyType getConflictStrategyType();
}
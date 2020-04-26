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
 * @author Bachar.RIMA
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
	AbstractConflictScope<T, E> getConflictScope();
	AbstractConflictCandidate<T, E> getConflictCandidate();
	AbstractConflictResolutionStrategy<T, E> getConflictStrategy();
	IConflictResolutionStrategyType getConflictStrategyType();
}
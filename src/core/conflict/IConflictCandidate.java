package core.conflict;

/**
 * an IConflictCandidate generic interface that defines a conflict candidate entity.<br><br> 
 * 
 * This interface provides operations for accessing/setting a conflict candidate's underlying entity,
 * and an operation to verify if it satisfies a conflict predicate. If it does then it 
 * can be considered as a conflict source entity for a conflict scope within a conflict domain.<br><br>
 * 
 * All entities having the same type as that of entities defining a conflict scope for a conflict domain,
 * are conflict candidates for this domain, and for the conflict scope in particular. A conflict candidate
 * entity has to fulfill a conflict predicate in order to be considered as a conflict source entity.
 * A conflict candidate entity might require having access to information provided by both the conflict
 * domain entity and its conflict scope.<br><br>
 * 
 * It must be implemented by any concrete class designating a conflict candidate.
 *  
 * @author Bachar Rima
 * @see IConflictDomain
 * @see AbstractConflictScope
 * @see AbstractConflictCandidate
 *
 * @param <T> The type of the conflict candidate entity (the same type 
 * of the entities that define the conflict scope for this conflict candidate entity).
 * @param <E> The type of entities that underly the conflict arising by the conflict source entity
 * of the conflict scope for this conflict candidate entity.
 */
public interface IConflictCandidate<T, E> {
	
	/* METHODS */
	/**
	 * Returns this conflict candidate's underlying entity
	 * @return this conflict candidate's underlying entity
	 */
	T getEntity();
	
	/**
	 * Sets this conflict candidate's underlying entity
	 * @param entity the value to set this conflict candidate's underlying entity
	 */
	void setEntity(T entity);
	
	/**
	 * Returns this conflict candidate's conflict scope
	 * @return this conflict candidate's conflict scope
	 */
	AbstractConflictScope<T, E> getConflictScope();
	
	/**
	 * Set this conflict candidate's conflict scope
	 * @param conflictScope the value to set this conflict candidate's conflict scope
	 */
	void setConflictScope(AbstractConflictScope<T, E> conflictScope);
	
	/**
	 * Checks whether this conflict candidate satisfies a conflict predicate with respect
	 * to its conflict scope
	 * @return true if this conflict candidate satisfies a conflict predicate, false otherwise.
	 */
	boolean satisfiesConflictCondition();
}
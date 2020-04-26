package core.conflict;

/**
 * an IConflictCandidate generic interface that defines a conflict candidate entity.<br><br> 
 * 
 * This interface provides operations for accessing/setting a conflict candidate's type,
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
 * @author Bachar.RIMA
 * @see IConflictDomain
 * @see AbstractConflictScope
 *
 * @param <T> The type of the conflict candidate entity.
 */
public interface IConflictCandidate<T> {
	
	/* METHODS */
	T getCandidate();
	void setCandidate(T candidate);
	boolean satisfiesConflictCondition();
}
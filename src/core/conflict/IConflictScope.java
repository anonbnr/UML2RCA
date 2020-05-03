package core.conflict;

import java.util.Collection;

/**
 * an IConflictScope generic interface that defines a conflict scope entity.<br><br> 
 * 
 * For a given conflict domain, if a conflict arises, then it must occur in a conflict scope within 
 * the conflict domain in hand. A conflict scope, for example, can designate a set of a classes 
 * within a generalization hierarchy.<br><br>
 * 
 * If a conflict arises, then it is caused by a conflict source entity, having the same type as the
 * entities defining a conflict scope for a conflict domain.<br><br>
 * 
 * It must be implemented by any concrete class designating a conflict scope.
 *  
 * @author Bachar Rima
 * @see IConflictDomain
 * @see AbstractConflictSource
 * @see AbstractConflictScope
 *
 * @param <T> The type of entities that define this conflict scope.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * of this conflict scope.
 */
public interface IConflictScope<T, E> {
	
	/* METHODS */
	/**
	 * Returns this conflict scope's conflict domain
	 * @return Returns this conflict scope's conflict domain
	 */
	IConflictDomain<T, E> getConflictDomain();
	
	/**
	 * Sets this conflict scope's conflict domain
	 * @param conflictDomain the value to set this conflict scope's conflict domain
	 */
	void setConflictDomain(IConflictDomain<T, E> conflictDomain);
	
	/**
	 * Returns this conflict scope's underlying scope entities
	 * @return this conflict scope's underlying scope entities
	 */
	Collection<T> getScope();
	
	/**
	 * Sets this conflict scope's underlying scope entities
	 * @param scope the value to set this conflict scope's underlying scope entities
	 */
	void setScope(Collection<T> scope);
	
	/**
	 * Returns this conflict scope's conflict source entity
	 * @return this conflict scope's conflict source entity
	 */
	AbstractConflictSource<T, E> getConflictSource();

	/**
	 * Sets this conflict scope's conflict source entity
	 * @param conflictSource the value to set this conflict scope's conflict source entity
	 */
	void setConflictSource(AbstractConflictSource<T, E> conflictSource);
}

package core.conflict;

import java.util.Collection;
/**
 * an AbstractConflictScope generic abstract class that defines a conflict scope entity.<br><br> 
 * 
 * For a given conflict domain, if a conflict arises, then it must occur in a conflict scope within 
 * the conflict domain in hand. A conflict scope, for example, can designate a set of a classes 
 * within a generalization hierarchy.<br><br>
 * 
 * If a conflict arises, then it is caused by a conflict source entity, having the same type as the
 * entities defining a conflict scope for a conflict domain.<br><br>
 * 
 * It must be specialized by all concrete conflict scope classes.
 *
 * @author Bachar.RIMA
 * @see IConflictDomain
 * @see AbstractConflictSource
 *
 * @param <T> The type of entities that define this conflict scope.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * of this conflict scope.
 */
public abstract class AbstractConflictScope<T, E> {
	
	/* ATTRIBUTES */
	protected IConflictDomain<T, E> conflictDomain;
	protected Collection<T> scope;
	protected AbstractConflictSource<T, E> conflictSource;
	
	/* CONSTRUCTOR */
	public AbstractConflictScope(IConflictDomain<T, E> conflictDomain, Collection<T> scope, 
			AbstractConflictSource<T, E> conflictSource) {
		
		this.conflictDomain = conflictDomain;
		this.scope = scope;
		this.conflictSource = conflictSource;
	}
	
	/* METHODS */
	public IConflictDomain<T, E> getConflictDomain() {
		return conflictDomain;
	}
	
	public Collection<T> getScope() {
		return scope;
	}
	
	public void setScope(Collection<T> scope) {
		this.scope = scope;
	}
	
	public AbstractConflictSource<T, E> getConflictSource() {
		return conflictSource;
	}

	public void setConflictSource(AbstractConflictSource<T, E> conflictSource) {
		this.conflictSource = conflictSource;
	}
}

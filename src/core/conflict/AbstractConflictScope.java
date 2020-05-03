package core.conflict;

import java.util.Collection;
/**
 * an AbstractConflictScope generic abstract class that is used to factor 
 * the common interface and state of all concrete conflict scope classes.<br><br> 
 * 
 * It must be specialized by all concrete conflict scope classes.
 *
 * @author Bachar Rima
 * @see IConflictDomain
 * @see AbstractConflictSource
 *
 * @param <T> The type of entities that define this conflict scope.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * of this conflict scope.
 */
public abstract class AbstractConflictScope<T, E> implements IConflictScope<T, E> {
	
	/* ATTRIBUTES */
	/**
	 * The conflict scope's conflict domain
	 */
	protected IConflictDomain<T, E> conflictDomain;
	
	/**
	 * The conflict scope's underlying scope entities that define it
	 */
	protected Collection<T> scope;
	
	/**
	 * The conflict scope's conflict source
	 */
	protected AbstractConflictSource<T, E> conflictSource;
	
	/* CONSTRUCTOR */
	/**
	 * Creates a conflict scope having conflictDomain as its conflict domain and scope as
	 * the underlying scope entities that define its scope
	 * @param conflictDomain a conflict domain to define this conflict scope's conflict domain
	 * @param scope the collection of entities to define this conflict scope's underlying scope
	 */
	public AbstractConflictScope(IConflictDomain<T, E> conflictDomain, Collection<T> scope) {
		
		this.conflictDomain = conflictDomain;
		this.scope = scope;
	}
	
	/* METHODS */
	@Override
	public IConflictDomain<T, E> getConflictDomain() {
		return conflictDomain;
	}
	
	@Override
	public void setConflictDomain(IConflictDomain<T, E> conflictDomain) {
		this.conflictDomain = conflictDomain;
	}
	
	@Override
	public Collection<T> getScope() {
		return scope;
	}
	
	@Override
	public void setScope(Collection<T> scope) {
		this.scope = scope;
	}
	
	@Override
	public AbstractConflictSource<T, E> getConflictSource() {
		return conflictSource;
	}

	@Override
	public void setConflictSource(AbstractConflictSource<T, E> conflictSource) {
		this.conflictSource = conflictSource;
	}
}

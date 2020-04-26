package core.conflict;

import java.util.Collection;

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

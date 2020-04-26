package core.conflict;

public class DiscardConflictResolutionStrategy<T, E> extends AbstractConflictResolutionStrategy<T, E> {
	
	public DiscardConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope) {
		super(target, conflictScope, true);
	}
	
	@Override
	public void resolve() {
		// do nothing and keep the element already owned by the target class
	}
}

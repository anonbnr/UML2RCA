package core.conflict;

/**
 * a DiscardConflictResolutionStrategy generic class used to define a generic discarding
 * conflict resolution strategy.<br><br>
 * 
 * This strategy consists of keeping the original elements of a conflict source entity, 
 * for a given conflict scope, while discarding the elements that gave rise to the
 * conflict, as well as the changes incurred by them on the conflict domain's state.
 * In this case, we consider that this behavior is justified by the fact that the original
 * elements and the elements that gave rise to the conflict share the same semantics. Therefore,
 * the conflicting-spawning elements are copies of the original elements, and can be safely discarded.<br><br>
 * 
 * @author Bachar.RIMA
 *
 * @param <T> The type of entities that define the conflict scope of this conflict resolution strategy.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * resolved by this conflict resolution strategy.
 */
public class DiscardConflictResolutionStrategy<T, E> extends AbstractConflictResolutionStrategy<T, E> {
	
	public DiscardConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope) {
		super(target, conflictScope, true);
	}
	
	@Override
	public void resolve() {
		// do nothing and keep the element already owned by the target class
	}
}

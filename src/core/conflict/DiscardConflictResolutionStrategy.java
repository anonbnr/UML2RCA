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
 * the conflict-spawning elements are copies of the original elements, and can be safely discarded.<br><br>
 * 
 * @author Bachar Rima
 *
 * @param <T> The type of entities that define the conflict scope of this conflict resolution strategy.
 * @param <E> The type of entities that underly the conflict incurred by the conflict source entity
 * resolved by this conflict resolution strategy.
 */
public class DiscardConflictResolutionStrategy<T, E> extends AbstractConflictResolutionStrategy<T, E> {
	
	/**
	 * Creates a discarding conflict resolution strategy having target as its target, and conflictScope
	 * as its conflict scope, while specifying that the pre/post transformation elements of 
	 * the conflict source of its conflict scope share the same semantics
	 * @param target an entity defining the target of this discarding conflict resolution strategy
	 * @param conflictScope a conflict scope defining the conflict scope of this discarding conflict 
	 * resolution strategy
	 */
	public DiscardConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope) {
		super(target, conflictScope, true);
	}
	
	/**
	 * Discards the conflicting source to resolve the conflict, since the pre/post transformation
	 * elements underlying the conflict share the same semantics
	 */
	@Override
	public void resolve() {
		
	}
}

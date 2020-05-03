package uml2rca.adaptation.generalization.conflict.resolution_strategy;

import org.eclipse.uml2.uml.NamedElement;

import core.conflict.AbstractConflictResolutionStrategy;
import core.conflict.AbstractConflictScope;

public abstract class AbstractRenameConflictResolutionStrategy<T, E extends NamedElement> 
	extends AbstractConflictResolutionStrategy<T, E> {
	
	/* CONSTRUCTOR */
	public AbstractRenameConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope) {
		
		super(target, conflictScope, false);
	}
	
	/* METHODS */
	@Override
	public void resolve() {
		conflictScope.getConflictSource()
		.getPostTransformationConflictingElements()
		.stream()
		.forEach(postTransformationConflictingAttribute -> 
			rename(postTransformationConflictingAttribute));
	}
	
	protected abstract void rename(E postTransformationConflictingElement);
}

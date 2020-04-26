package uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictScope;
import core.conflict.DiscardConflictResolutionStrategy;

public class DiscardConflictingAttributeConflictResolutionStrategy extends DiscardConflictResolutionStrategy<Class, Property> {

	public DiscardConflictingAttributeConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Property> conflictScope) {
		super(target, conflictScope);
		resolve();
	}
}

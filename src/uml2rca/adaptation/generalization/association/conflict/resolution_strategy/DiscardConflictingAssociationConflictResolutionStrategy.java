package uml2rca.adaptation.generalization.association.conflict.resolution_strategy;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictScope;
import core.conflict.DiscardConflictResolutionStrategy;

public class DiscardConflictingAssociationConflictResolutionStrategy extends DiscardConflictResolutionStrategy<Class, Association> {

	public DiscardConflictingAssociationConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Association> conflictScope) {
		super(target, conflictScope);
		resolve();
	}
}

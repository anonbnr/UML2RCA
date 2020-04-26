package uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictScope;
import core.conflict.DiscardConflictResolutionStrategy;

public class DiscardConflictingDependencyConflictResolutionStrategy extends DiscardConflictResolutionStrategy<Class, Dependency> {

	public DiscardConflictingDependencyConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Dependency> conflictScope) {
		super(target, conflictScope);
		resolve();
	}
}

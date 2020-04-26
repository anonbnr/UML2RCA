package uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractExpertRenameConflictResolutionStrategy;
import uml2rca.exceptions.ConflictingNamesAndProvidedNamesSizesMismatchException;

public class ExpertRenameDependencyConflictResolutionStrategy extends AbstractExpertRenameConflictResolutionStrategy<Class, Dependency> {

	/* CONSTRUCTOR */
	public ExpertRenameDependencyConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Dependency> conflictScope,
			List<String> expertProvidedNames) throws ConflictingNamesAndProvidedNamesSizesMismatchException {
		
		super(target, conflictScope, expertProvidedNames);
		resolve();
	}
}

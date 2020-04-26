package uml2rca.adaptation.generalization.association.conflict.resolution_strategy;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractExpertRenameConflictResolutionStrategy;
import uml2rca.exceptions.ConflictingNamesAndProvidedNamesSizesMismatchException;

public class ExpertRenameAssociationConflictResolutionStrategy extends AbstractExpertRenameConflictResolutionStrategy<Class, Association> {
	
	/* CONSTRUCTOR */
	public ExpertRenameAssociationConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Association> conflictScope, 
			List<String> expertProvidedNames) throws ConflictingNamesAndProvidedNamesSizesMismatchException {
		
		super(target, conflictScope, expertProvidedNames);
		resolve();
	}
}

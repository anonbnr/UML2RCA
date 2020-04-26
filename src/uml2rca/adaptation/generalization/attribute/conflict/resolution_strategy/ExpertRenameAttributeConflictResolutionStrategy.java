package uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractExpertRenameConflictResolutionStrategy;
import uml2rca.exceptions.ConflictingNamesAndProvidedNamesSizesMismatchException;

public class ExpertRenameAttributeConflictResolutionStrategy extends AbstractExpertRenameConflictResolutionStrategy<Class, Property> {

	/* CONSTRUCTOR */
	public ExpertRenameAttributeConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Property> conflictScope, 
			List<String> expertProvidedNames) throws ConflictingNamesAndProvidedNamesSizesMismatchException {
		
		super(target, conflictScope, expertProvidedNames);
		resolve();
	}
}

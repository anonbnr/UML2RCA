package uml2rca.adaptation.generalization.conflict.resolution_strategy;

import java.util.List;

import org.eclipse.uml2.uml.NamedElement;

import core.conflict.AbstractConflictScope;
import uml2rca.exceptions.ConflictingNamesAndProvidedNamesSizesMismatchException;

public abstract class AbstractExpertRenameConflictResolutionStrategy<T, E extends NamedElement> 
	extends AbstractRenameConflictResolutionStrategy<T, E> {
	
	/* ATTRIBUTES */
	protected List<String> expertProvidedNames;

	/* CONSTRUCTOR */
	public AbstractExpertRenameConflictResolutionStrategy(T target, AbstractConflictScope<T, E> conflictScope, List<String> expertProvidedNames)
					throws ConflictingNamesAndProvidedNamesSizesMismatchException {
		
		super(target, conflictScope);
		
		if (conflictScope.getConflictSource().getPreTransformationConflictingElements().size() != expertProvidedNames.size())
			throw new ConflictingNamesAndProvidedNamesSizesMismatchException(
					"The number of conflicting attributes is "
					+ conflictScope.getConflictSource().getPreTransformationConflictingElements().size()
					+ ", whereas the number of provided names is "
					+ expertProvidedNames.size());
		
		this.expertProvidedNames = expertProvidedNames;
	}
	
	@Override
	protected void rename(E postTransformationConflictingElement) {
		int index = conflictScope.getConflictSource()
				.getPostTransformationConflictingElements()
				.indexOf(postTransformationConflictingElement);
		
		String providedName = expertProvidedNames.get(index);
		postTransformationConflictingElement.setName(providedName);
	}
}

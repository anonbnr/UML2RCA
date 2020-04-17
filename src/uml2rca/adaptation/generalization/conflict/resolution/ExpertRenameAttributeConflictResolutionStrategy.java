package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import uml2rca.exceptions.ConflictingNamesAndProvidedNamesSizesMismatchException;

public class ExpertRenameAttributeConflictResolutionStrategy extends AttributeConflictResolutionStrategy {
	
	private List<String> expertProvidedNames;

	public ExpertRenameAttributeConflictResolutionStrategy(Class target, 
			List<Property> conflicting, List<String> expertProvidedNames) 
					throws ConflictingNamesAndProvidedNamesSizesMismatchException {
		super(target, conflicting, false);
		
		if (conflicting.size() != expertProvidedNames.size())
			throw new ConflictingNamesAndProvidedNamesSizesMismatchException(
					"The number of conflicting attributes is "
					+ conflicting.size()
					+ ", whereas the number of provided names is "
					+ expertProvidedNames.size());
		
		this.expertProvidedNames = expertProvidedNames;
		resolve();
	}

	@Override
	protected void resolve() {
		target.getOwnedAttribute(conflicting.get(0).getName(), 
				conflicting.get(0).getType()).destroy();
		
		for (int i = 0; i < conflicting.size(); i++)
			target.createOwnedAttribute(
					expertProvidedNames.get(i), conflicting.get(i).getType());
	}
}

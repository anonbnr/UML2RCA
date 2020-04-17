package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import uml2rca.java.extensions.utility.Strings;

public class DefaultRenameAttributeConflictResolutionStrategy extends AttributeConflictResolutionStrategy {

	public DefaultRenameAttributeConflictResolutionStrategy(Class target, List<Property> conflicting) {
		super(target, conflicting, false);
		resolve();
	}

	@Override
	protected void resolve() {
		target.getOwnedAttribute(conflicting.get(0).getName(), 
				conflicting.get(0).getType()).destroy();
		
		for (Property conflictingAttribute: conflicting) {
			Class owningClass = conflictingAttribute.getClass_();
			String newAttributeName = Strings.decapitalize(owningClass.getName())
					+ Strings.capitalize(conflictingAttribute.getName());
			target.createOwnedAttribute(newAttributeName, conflictingAttribute.getType());
		}
	}
}

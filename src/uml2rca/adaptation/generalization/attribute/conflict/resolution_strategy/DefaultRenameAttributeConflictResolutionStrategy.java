package uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractRenameConflictResolutionStrategy;
import uml2rca.java.extensions.utility.Strings;

public class DefaultRenameAttributeConflictResolutionStrategy extends AbstractRenameConflictResolutionStrategy<Class, Property> {

	public DefaultRenameAttributeConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Property> conflictScope) {
		super(target, conflictScope);
		resolve();
	}
	
	@Override
	protected void rename(Property postTransformationConflictingAttribute) {
		int index = conflictScope.getConflictSource()
				.getPostTransformationConflictingElements()
				.indexOf(postTransformationConflictingAttribute);
		
		Property preTransformationConflictingAttribute = conflictScope.getConflictSource()
				.getPreTransformationConflictingElements()
				.get(index);
		
		Class owningClass = preTransformationConflictingAttribute.getClass_();
		
		postTransformationConflictingAttribute.setName(Strings.decapitalize(owningClass.getName())
				+ Strings.capitalize(preTransformationConflictingAttribute.getName()));
	}
}

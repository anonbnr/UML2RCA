package uml2rca.adaptation.generalization.association.conflict.resolution_strategy;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractRenameConflictResolutionStrategy;

public class DefaultRenameAssociationConflictResolutionStrategy extends AbstractRenameConflictResolutionStrategy<Class, Association> {

	public DefaultRenameAssociationConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Association> conflictScope) {
		super(target, conflictScope);
		resolve();
	}

	@Override
	protected void rename(Association postTransformationConflictingAssociation) {
		int index = conflictScope
				.getConflictSource()
				.getPostTransformationConflictingElements()
				.indexOf(postTransformationConflictingAssociation);
		
		Association preTransformationConflictingAssociation = conflictScope
				.getConflictSource()
				.getPreTransformationConflictingElements()
				.get(index);
		
		Class originalOwningClass = null;
		
		if (preTransformationConflictingAssociation.getEndTypes().contains(conflictScope.getConflictSource().getEntity()))
			originalOwningClass = conflictScope.getConflictSource().getEntity();
		else 
			originalOwningClass = (Class) preTransformationConflictingAssociation.getEndTypes()
					.stream()
					.filter(type -> 
						type != conflictScope.getConflictSource().getEntity() 
							&& conflictScope.getScope().contains(type))
					.findFirst()
					.get();
		
		postTransformationConflictingAssociation.setName(
				postTransformationConflictingAssociation.getName() 
				 + "--"
				 + originalOwningClass.getName());
	}
}

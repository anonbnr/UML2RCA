package uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy;

import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.conflict.resolution_strategy.AbstractRenameConflictResolutionStrategy;

public class DefaultRenameDependencyConflictResolutionStrategy extends AbstractRenameConflictResolutionStrategy<Class, Dependency> {

	public DefaultRenameDependencyConflictResolutionStrategy(Class target, AbstractConflictScope<Class, Dependency> conflictScope) {
		super(target, conflictScope);
		resolve();
	}

	@Override
	protected void rename(Dependency postTransformationConflictingDependency) {
		int index = conflictScope
				.getConflictSource()
				.getPostTransformationConflictingElements()
				.indexOf(postTransformationConflictingDependency);
		
		Dependency preTransformationConflictingDependency = conflictScope
				.getConflictSource()
				.getPreTransformationConflictingElements()
				.get(index);
		
		Class originalOwningClass = null;
		
		if (preTransformationConflictingDependency.getClients().contains(conflictScope.getConflictSource().getEntity())
				|| preTransformationConflictingDependency.getSuppliers().contains(conflictScope.getConflictSource().getEntity()))
			originalOwningClass = conflictScope.getConflictSource().getEntity();
		
		else
			originalOwningClass = (Class) Stream
			.concat(preTransformationConflictingDependency.getClients().stream(),
					preTransformationConflictingDependency.getSuppliers().stream())
			.filter(namedElement -> 
				namedElement != conflictScope.getConflictSource().getEntity()
					&& conflictScope.getScope().contains(namedElement))
			.findFirst()
			.get();
		
		postTransformationConflictingDependency.setName(
				postTransformationConflictingDependency.getName() 
				 + "--"
				 + originalOwningClass.getName());
	}
}

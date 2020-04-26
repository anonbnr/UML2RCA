package uml2rca.adaptation.generalization;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;

import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.AssociationConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.AttributeConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.DependencyConflictResolutionStrategyType;
import uml2rca.exceptions.ConflictResolutionStrategyException;
import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;

public class MultipleGeneralizationAdaptation extends SimpleGeneralizationAdaptation {

	/* CONSTRUCTOR */
	public MultipleGeneralizationAdaptation(Class leaf, Class choice)
			throws NotALeafInGeneralizationHierarchyException, NotAValidLevelForGeneralizationAdaptationException {
		preTransformInit(leaf, choice);
		conflictScope = Stream
				.concat(visitableSource.getSuperClasses().stream(), 
						visitableSource.getSubClasses().stream())
				.collect(Collectors.toList());
		
		this.setTarget(this.transform(choice));
		postTransformationClean();
	}
	
	/* METHODS */
	@Override
	protected void initTargetClassAttributes()
			throws ConflictResolutionStrategyException {
		/* owned attributes */
		initTargetClassSourceAttributes(AttributeConflictResolutionStrategyType.NONE);
		
		/* inherited attributes (superclasses) */
		initTargetClassInheritedAttributes(AttributeConflictResolutionStrategyType.DEFAULT_RENAME);
		
		/* specializing attributes (subclasses) */
		initTargetClassSpecializingAttributes(AttributeConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}
	
	@Override
	protected void initTargetClassAssociations() {
		/* owned associations */
		initTargetClassSourceAssociations(AssociationConflictResolutionStrategyType.NONE);
		
		/* inherited associations (superclasses) */
		initTargetClassInheritedAssociations(AssociationConflictResolutionStrategyType.DEFAULT_RENAME);
		
		/* specializing associations (subclasses) */
		initTargetClassSpecializingAssociations(AssociationConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}
	
	@Override
	protected void initTargetClassDependencies() {
		/* owned dependencies */
		initTargetClassSourceDependencies(DependencyConflictResolutionStrategyType.NONE);
		
		/* inherited dependencies (superclasses) */
		initTargetClassInheritedDependencies(DependencyConflictResolutionStrategyType.DEFAULT_RENAME);
		
		/* specializing dependencies (subclasses) */
		initTargetClassSpecializingDependencies(DependencyConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}
}

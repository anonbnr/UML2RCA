package uml2rca.adaptation.generalization.visitor;

import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.adaptation.generalization.association.conflict.GeneralizationAdaptationClassAssociationClassConflictCandidate;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.AssociationConflictResolutionStrategyType;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

public class GeneralizationAdaptationAssociationClassVisitor extends GeneralizationAdaptationAssociationVisitor {

	/* CONSTRUCTOR */
	public GeneralizationAdaptationAssociationClassVisitor(GeneralizationAdaptationClassVisitor sourceClassVisitor,
			Collection<Class> scope, AssociationConflictResolutionStrategyType conflictStrategyType) {
		
		super(sourceClassVisitor, scope, conflictStrategyType);
		this.conflictCandidate = new GeneralizationAdaptationClassAssociationClassConflictCandidate(this, conflictScope, 
				sourceClassVisitor.getOwner());
	}
	
	/* METHODS */
	@Override
	public void initTargetClassNonConflictingAssociation() {
		AssociationClass associationClass = (AssociationClass) this.visitedElement;
		AssociationClass newOwnedAssociation = UMLFactory.eINSTANCE.createAssociationClass();
		newOwnedAssociation.setPackage(associationClass.getPackage());
		newOwnedAssociation.setName(associationClass.getName());
		
		associationClass.getOwnedAttributes()
		.stream()
		.filter(attribute -> 
			!associationClass.getMemberEnds()
				.stream()
				.map(Property::getName)
				.collect(Collectors.toList())
				.contains(attribute.getName()))
		.forEach(attribute -> 
			newOwnedAssociation.createOwnedAttribute(attribute.getName(), attribute.getType()));
		
		if (Associations.isReflexive(visitedElement))
			initTargetClassReflexiveAssociation(newOwnedAssociation);
		
		else
			initTargetClassNonReflexiveAssociation(newOwnedAssociation);
	}
}

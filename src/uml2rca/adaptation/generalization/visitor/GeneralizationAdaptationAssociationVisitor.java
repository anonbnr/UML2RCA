package uml2rca.adaptation.generalization.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.adaptation.generalization.association.conflict.GeneralizationAdaptationClassAssociationConflictCandidate;
import uml2rca.adaptation.generalization.association.conflict.GeneralizationAdaptationClassAssociationConflictScope;
import uml2rca.adaptation.generalization.association.conflict.OwningClassAssociationConflictSource;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.AssociationConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.DefaultRenameAssociationConflictResolutionStrategy;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.DiscardConflictingAssociationConflictResolutionStrategy;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.ExpertRenameAssociationConflictResolutionStrategy;
import uml2rca.exceptions.ConflictResolutionStrategyException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.visitor.IVisitableUMLElement;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableAssociation;

public class GeneralizationAdaptationAssociationVisitor extends GeneralizationAdaptationClassAbstractVisitor<Association> {
	
	/* CONSTRUCTOR */
	public GeneralizationAdaptationAssociationVisitor(GeneralizationAdaptationClassVisitor sourceClassVisitor, 
			Collection<Class> scope, AssociationConflictResolutionStrategyType conflictStrategyType) {
		
		super(sourceClassVisitor, conflictStrategyType);
		this.conflictScope = new GeneralizationAdaptationClassAssociationConflictScope(this, scope,
				new OwningClassAssociationConflictSource(sourceClassVisitor.getOwner()));
		
		this.conflictCandidate = new GeneralizationAdaptationClassAssociationConflictCandidate(conflictScope, 
				sourceClassVisitor.getOwner(), this);
	}

	/* METHODS */
	public String getPostAdaptationIndirectlyOwnedAssociationDefaultName() {
		return visitedElement.getName() + "-" + sourceClassVisitor.getTarget().getName();
	}
	
	@Override
	public void visit(IVisitableUMLElement element) { // visits an owner's association
		VisitableAssociation visitableAssociation = (VisitableAssociation) element;
		visitedElement = visitableAssociation.getContainedAssociation();
		
		try {
			initTargetClassAssociation();
		} catch (ConflictResolutionStrategyException e) {
			e.printStackTrace();
		}
	}

	protected void initTargetClassAssociation() throws ConflictResolutionStrategyException {
		
		/*
		 * if the current association relates source to one of its subclasses, 
		 * ignore it at the level of the subclass, since it will be dealt with
		 * at the level of source
		 */
		if (sourceClassVisitor.getVisitableSource().getSubClasses().contains(sourceClassVisitor.getOwner()) 
				&& sourceClassVisitor.getSource().getAssociations().contains(visitedElement))
			return;
		
		if (conflictCandidate.satisfiesConflictCondition()) {
			conflictScope.setConflictSource(new OwningClassAssociationConflictSource(sourceClassVisitor.getOwner()));
			initAssociationConflictResolutionStrategy();
		}
		
		else
			initTargetClassNonConflictingAssociation();
		
		
		if (toClean(visitedElement))
			toClean.add(visitedElement);
	}

	protected void initTargetClassNonConflictingAssociation() {
		Association newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
		
		newOwnedAssociation.setPackage(visitedElement.getPackage());
		newOwnedAssociation.setName(visitedElement.getName());
		
		if (Associations.isReflexive(visitedElement))
			initTargetClassReflexiveAssociation(newOwnedAssociation);
		
		else
			initTargetClassNonReflexiveAssociation(newOwnedAssociation);
	}

	protected void initTargetClassReflexiveAssociation(Association newOwnedAssociation) {
		Property newMemberEnd;
		
		for (Property memberEnd: visitedElement.getMemberEnds()) {
			
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
			
			if (sourceClassVisitor.getOwner() == sourceClassVisitor.getSource())
				newMemberEnd.setType(sourceClassVisitor.getTarget());
			else if (memberEnd == visitedElement.getMemberEnds().get(0)) {
				newMemberEnd.setName(Strings.decapitalize(sourceClassVisitor.getSource().getName()));
				newMemberEnd.setType(sourceClassVisitor.getTarget());
				newOwnedAssociation.setName(
						getPostAdaptationIndirectlyOwnedAssociationDefaultName());
			}
		}
	}
	
	protected void initTargetClassNonReflexiveAssociation(Association newOwnedAssociation) {
		Property newMemberEnd;
		
		for (Property memberEnd: visitedElement.getMemberEnds()) {
			
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
			
			if (memberEnd.getType() == sourceClassVisitor.getOwner() && 
					sourceClassVisitor.getOwner() == sourceClassVisitor.getSource())
				newMemberEnd.setType(sourceClassVisitor.getTarget());
			
			/*
			 * if the current association relates source to one of its subclasses, 
			 * then the new association will relate source to itself, since the subclass
			 * will be collapsed into the target class
			 */
			if (sourceClassVisitor.getVisitableSource().getSubClasses().contains(memberEnd.getType()))
				newMemberEnd.setType(sourceClassVisitor.getTarget());
			
			if (memberEnd.getType() == sourceClassVisitor.getOwner() 
					&& sourceClassVisitor.getOwner() != sourceClassVisitor.getSource()) {
				newMemberEnd.setName(Strings.decapitalize(sourceClassVisitor.getSource().getName()));
				newMemberEnd.setType(sourceClassVisitor.getTarget());
				newOwnedAssociation.setName(getPostAdaptationIndirectlyOwnedAssociationDefaultName());
			}
		}
	}

	protected void initAssociationConflictResolutionStrategy() throws ConflictResolutionStrategyException {
		initConflictSourcePreTransformationConflictingAssociations();
		
		if (conflictStrategyType == AssociationConflictResolutionStrategyType.DEFAULT_RENAME) {
			initConflictSourcePostTransformationConflictingAssociations();
			conflictStrategy = new DefaultRenameAssociationConflictResolutionStrategy(sourceClassVisitor.getTarget(), conflictScope);
		}
		
		else if (conflictStrategyType == AssociationConflictResolutionStrategyType.EXPERT_RENAME) {
			initConflictSourcePostTransformationConflictingAssociations();
			List<String> expertProvidedNames = Arrays.asList(
					"expertNameProvidedForOriginallyOwnedAssociation",
					"expertNameProvidedForConflictingAssociation");
			
			conflictStrategy = new ExpertRenameAssociationConflictResolutionStrategy(sourceClassVisitor.getTarget(), 
					conflictScope, expertProvidedNames);
		}
		
		else if (conflictStrategyType == AssociationConflictResolutionStrategyType.EXPERT_RENAME)
			conflictStrategy = new DiscardConflictingAssociationConflictResolutionStrategy(sourceClassVisitor.getTarget(), conflictScope);
	}
	
	protected void initConflictSourcePreTransformationConflictingAssociations() {
		
		List<MutablePair<String, Type>> nonConflictingClassMemberEnds = 
				Associations.getOtherEndsInAssociationAsPairs(visitedElement, sourceClassVisitor.getOwner());
		
		conflictScope.getConflictSource().addPreTransformationConflictingElement(
				getPreAdaptationIndirectlyOwnedAssociation(nonConflictingClassMemberEnds));
		
		conflictScope.getConflictSource().addPreTransformationConflictingElement(visitedElement);
	}

	protected Association getPreAdaptationIndirectlyOwnedAssociation(List<MutablePair<String, Type>> nonConflictingClassMemberEnds) {
		Association originalAssociation = null;
		
		for (Class scopeClass: conflictScope.getScope())
			for (Association ownedAssociation: scopeClass.getAssociations())
				if (scopeClass != conflictScope.getConflictSource().getSource()
					&& ownedAssociation.getName().equals(visitedElement.getName())
					&& nonConflictingClassMemberEnds
					.stream()
					.allMatch(memberEnd -> 
						ownedAssociation.getMemberEnd(memberEnd.getLeft(), memberEnd.getRight()) != null)) {
					originalAssociation = ownedAssociation;
					break;
				}
		
		return originalAssociation;
	}
	
	protected void initConflictSourcePostTransformationConflictingAssociations() {
		initTargetClassNonConflictingAssociation();
		
		sourceClassVisitor.getTarget()
				.getAssociations()
				.stream()
				.filter(ownedAssociation -> ownedAssociation.getName().equals(
						getPostAdaptationIndirectlyOwnedAssociationDefaultName()))
				.forEach(conflictingAssociation -> 
					conflictScope.getConflictSource().addPostTransformationConflictingElement(conflictingAssociation));
	}
	
	@Override
	protected boolean toClean(Association association) {
		return !toClean.contains(association)
				&& (sourceClassVisitor.getOwner() == sourceClassVisitor.getSource() 
					|| sourceClassVisitor.getVisitableSource().getSubClasses()
						.contains(sourceClassVisitor.getOwner()));
	}
}

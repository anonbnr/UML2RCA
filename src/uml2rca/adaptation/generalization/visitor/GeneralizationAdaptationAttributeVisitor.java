package uml2rca.adaptation.generalization.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import uml2rca.adaptation.generalization.attribute.conflict.GeneralizationAdaptationClassAttributeConflictCandidate;
import uml2rca.adaptation.generalization.attribute.conflict.GeneralizationAdaptationClassAttributeConflictScope;
import uml2rca.adaptation.generalization.attribute.conflict.OwningClassAttributeConflictSource;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.AttributeConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.DefaultRenameAttributeConflictResolutionStrategy;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.DiscardConflictingAttributeConflictResolutionStrategy;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.ExpertRenameAttributeConflictResolutionStrategy;
import uml2rca.exceptions.ConflictResolutionStrategyException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.java.uml2.uml.extensions.visitor.IVisitableUMLElement;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableAttribute;
import uml2rca.model.management.EcoreModelManager;

public class GeneralizationAdaptationAttributeVisitor extends GeneralizationAdaptationClassAbstractVisitor<Property> {
	
	/* CONSTRUCTORS */
	public GeneralizationAdaptationAttributeVisitor(GeneralizationAdaptationClassVisitor sourceClassVisitor,
			Collection<Class> scope, AttributeConflictResolutionStrategyType conflictStrategyType) {
		
		super(sourceClassVisitor, conflictStrategyType);
		this.conflictScope = new GeneralizationAdaptationClassAttributeConflictScope(this, scope);
		
		this.conflictCandidate = new GeneralizationAdaptationClassAttributeConflictCandidate(this, 
				conflictScope, sourceClassVisitor.getOwner());
	}

	/* METHODS */
	@Override
	public void visit(IVisitableUMLElement element) { // visits an owner's attribute
		VisitableAttribute visitableAttribute = (VisitableAttribute) element;
		visitedElement = visitableAttribute.getContainedAttribute();
		
		try {
			initTargetClassAttribute();
		} catch (ConflictResolutionStrategyException e) {
			e.printStackTrace();
		}
	}
	
	protected void initTargetClassAttribute() throws ConflictResolutionStrategyException {
		
		if (sourceClassVisitor.getVisitableSource().getSubClasses().contains(sourceClassVisitor.getOwner())
				&& !hasBooleanSubClassAttribute())
			createBooleanSubClassAttribute();
		
		if (conflictCandidate.satisfiesConflictCondition()) {
			conflictScope.setConflictSource(
					new OwningClassAttributeConflictSource(conflictScope, sourceClassVisitor.getOwner()));
			initAttributeConflictResolutionStrategy();
		}
		
		else
			initTargetClassNonConflictingAttribute();
	}
	
	protected void initTargetClassNonConflictingAttribute() {
		sourceClassVisitor.getTarget().createOwnedAttribute(visitedElement.getName(), visitedElement.getType());
	}
	
	protected void initAttributeConflictResolutionStrategy() throws ConflictResolutionStrategyException {
		initConflictSourcePreTransformationConflictingAttributes();
		
		if (conflictStrategyType == AttributeConflictResolutionStrategyType.DEFAULT_RENAME) {
			initConflictSourcePostTransformationConflictingAttributes();
			conflictStrategy = new DefaultRenameAttributeConflictResolutionStrategy(
					sourceClassVisitor.getTarget(), conflictScope);
		}
			
		else if (conflictStrategyType == AttributeConflictResolutionStrategyType.EXPERT_RENAME) {
			initConflictSourcePostTransformationConflictingAttributes();
			List<String> expertProvidedNames = Arrays.asList(
					"expertNameProvidedForOriginallyOwnedAttribute",
					"expertNameProvidedForConflictingAttribute");
			
			conflictStrategy = new ExpertRenameAttributeConflictResolutionStrategy(
					sourceClassVisitor.getTarget(), conflictScope, expertProvidedNames);
		}
			
		else if (conflictStrategyType == AttributeConflictResolutionStrategyType.DISCARD)
			conflictStrategy = new DiscardConflictingAttributeConflictResolutionStrategy(
					sourceClassVisitor.getTarget(), conflictScope);
	}
	
	protected boolean hasBooleanSubClassAttribute() {
		return Classes.hasAttribute(sourceClassVisitor.getTarget(), 
						Strings.decapitalize(sourceClassVisitor.getOwner().getName()), 
						EcoreModelManager.UML_PRIMITIVE_TYPES_LIBRARY.getOwnedType("Boolean"));
	}
	
	protected void createBooleanSubClassAttribute() {
		sourceClassVisitor.getTarget().createOwnedAttribute(
				Strings.decapitalize(sourceClassVisitor.getOwner().getName()), 
				EcoreModelManager.UML_PRIMITIVE_TYPES_LIBRARY.getOwnedType("Boolean"));
	}
	
	protected void initConflictSourcePreTransformationConflictingAttributes() {
		conflictScope.getConflictSource().addPreTransformationConflictingElement(
				getPreAdaptationIndirectlyOwnedAttribute());
		
		conflictScope.getConflictSource().addPreTransformationConflictingElement(visitedElement);
	}
	
	protected Property getPreAdaptationIndirectlyOwnedAttribute() {
		for (Class scopeClass: conflictScope.getScope())
			if (scopeClass != conflictScope.getConflictSource().getEntity())
				for (Property ownedAttribute: scopeClass.getOwnedAttributes())
					if (ownedAttribute.getName().equals(visitedElement.getName())
					&& ownedAttribute.getType() == visitedElement.getType())
						return ownedAttribute;
		
		return null;
	}
	
	protected void initConflictSourcePostTransformationConflictingAttributes() {
		initTargetClassNonConflictingAttribute();
		
		sourceClassVisitor.getTarget()
				.getOwnedAttributes()
				.stream()
				.filter(ownedAttribute -> ownedAttribute.getName().equals(visitedElement.getName())
						&& ownedAttribute.getType() == visitedElement.getType())
				.forEach(conflictingAttribute -> 
					conflictScope.getConflictSource()
						.addPostTransformationConflictingElement(conflictingAttribute));
	}
	
	@Override
	public boolean toClean(Property attribute) {
		return false;
	}
}

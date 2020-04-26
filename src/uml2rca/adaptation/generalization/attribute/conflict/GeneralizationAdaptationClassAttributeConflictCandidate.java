package uml2rca.adaptation.generalization.attribute.conflict;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictCandidate;
import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAttributeVisitor;
import uml2rca.java.uml2.uml.extensions.utility.Classes;

public class GeneralizationAdaptationClassAttributeConflictCandidate extends AbstractConflictCandidate<Class, Property> {
	
	/* ATTRIBUTES */
	protected GeneralizationAdaptationAttributeVisitor attributeVisitor;
	
	/* CONSTRUCTORS */
	public GeneralizationAdaptationClassAttributeConflictCandidate(
			GeneralizationAdaptationAttributeVisitor attributeVisitor,
			AbstractConflictScope<Class, Property> conflictScope,
			Class candidate) {
		
		super(conflictScope, candidate);
		this.attributeVisitor = attributeVisitor;
	}
	
	/* METHODS */
	@Override
	public boolean satisfiesConflictCondition() {
		return Classes.hasAttribute(
				attributeVisitor.getSourceClassVisitor().getTarget(), 
				attributeVisitor.getVisitedElement().getName(), 
				attributeVisitor.getVisitedElement().getType());
	}
}

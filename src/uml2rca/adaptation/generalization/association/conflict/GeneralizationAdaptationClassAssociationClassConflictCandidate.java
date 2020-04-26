package uml2rca.adaptation.generalization.association.conflict;

import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAssociationVisitor;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;

public class GeneralizationAdaptationClassAssociationClassConflictCandidate
		extends GeneralizationAdaptationClassAssociationConflictCandidate {

	public GeneralizationAdaptationClassAssociationClassConflictCandidate(
			GeneralizationAdaptationAssociationVisitor associationVisitor,
			AbstractConflictScope<Class, Association> conflictScope,
			Class candidate) {
		
		super(conflictScope, candidate, associationVisitor);
	}
	
	@Override
	public boolean satisfiesConflictCondition() {
		List<MutablePair<String, Type>> nonConflictingClassMemberEnds = 
				Associations.getOtherEndsInAssociationAsPairs(associationVisitor.getVisitedElement(), 
						associationVisitor.getSourceClassVisitor().getOwner());
		
		AssociationClass associationClass = (AssociationClass) associationVisitor.getVisitedElement();
		List<Property> associationClassAttributes = associationClass.getOwnedAttributes();
		
		return Classes.hasAssociationClass(associationVisitor.getSourceClassVisitor().getTarget(), 
				associationVisitor.getPostAdaptationIndirectlyOwnedAssociationDefaultName(), 
				nonConflictingClassMemberEnds, 
				associationClassAttributes);
	}
}

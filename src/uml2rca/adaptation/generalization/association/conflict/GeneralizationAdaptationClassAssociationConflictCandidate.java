package uml2rca.adaptation.generalization.association.conflict;

import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import core.conflict.AbstractConflictCandidate;
import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAssociationVisitor;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;

public class GeneralizationAdaptationClassAssociationConflictCandidate extends AbstractConflictCandidate<Class, Association> {
	
	/* ATTRIBUTES */
	protected GeneralizationAdaptationAssociationVisitor associationVisitor;
	
	/* CONSTRUCTORS */
	public GeneralizationAdaptationClassAssociationConflictCandidate(
			AbstractConflictScope<Class, Association> conflictScope,
			Class candidate,
			GeneralizationAdaptationAssociationVisitor associationVisitor) {
		
		super(conflictScope, candidate);
		this.associationVisitor = associationVisitor;
	}
	
	/* METHODS */
	@Override
	public boolean satisfiesConflictCondition() {
		List<MutablePair<String, Type>> nonConflictingClassMemberEnds = 
				Associations.getOtherEndsInAssociationAsPairs(associationVisitor.getVisitedElement(), 
						associationVisitor.getSourceClassVisitor().getOwner());
		
		return Classes.hasAssociation(associationVisitor.getSourceClassVisitor().getTarget(), 
				associationVisitor.getPostAdaptationIndirectlyOwnedAssociationDefaultName(), 
				nonConflictingClassMemberEnds);
	}
}

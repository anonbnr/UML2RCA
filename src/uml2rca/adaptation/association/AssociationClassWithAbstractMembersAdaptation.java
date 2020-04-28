package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

public class AssociationClassWithAbstractMembersAdaptation extends AssociationWithAbstractMembersAdaptation {

	public AssociationClassWithAbstractMembersAdaptation(Association source)
			throws NotAnAssociationWithAnAbstractMemberException {
		
		super(source);
	}
	
	@Override
	protected Association initTargetAssociation(Association currentAssociation) {
		AssociationClass currentAssociationClass = (AssociationClass) currentAssociation;
		AssociationClass newOwnedAssociationClass = UMLFactory.eINSTANCE.createAssociationClass();
		Property newMemberEnd;
		
		for (Property memberEnd: currentAssociation.getMemberEnds()) {
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociationClass, newMemberEnd, memberEnd.isNavigable());
		}
		
		currentAssociationClass.getOwnedAttributes()
		.stream()
		.forEach(attribute -> 
			newOwnedAssociationClass.createOwnedAttribute(attribute.getName(), attribute.getType()));
		
		return newOwnedAssociationClass;
	}
}

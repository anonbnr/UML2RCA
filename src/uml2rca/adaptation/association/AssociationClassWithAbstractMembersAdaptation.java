package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnAssociationClassException;
import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an AssociationClassWithAbstractMembersAdaptation concrete class that is used to adapt a UML
 * association class having abstract members by moving down the association class into each non abstract child 
 * of each of its abstract members.<br><br>
 * 
 * The adaptation consists of creating a list of target association classes having the 
 * same owning package as the source association class to adapt, such that the members of each 
 * target association class consist of the original non abstract members of the source association 
 * class and a non abstract child of the original abstract member of the source association class.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class AssociationClassWithAbstractMembersAdaptation extends AssociationWithAbstractMembersAdaptation {

	/* CONSTRUCTOR */
	/**
	 * Creates an association class with abstract members adaptation having source as its source
	 * association class to adapt, then applies the adaptation to obtain the list of target
	 * association classes with no abstract members, such that the members of each 
	 * target association class consist of the original non abstract members of the source association 
	 * class and a non abstract child of the original abstract member of the source association class.
	 * It then cleans the post-adaptation residues.
	 * @param source the source association class to adapt
	 * @throws NotAnAssociationClassException if the provided source entity is not an association class
	 * @throws NotAnAssociationWithAnAbstractMemberException if the provided source association 
	 * doesn't have abstract members
	 */
	public AssociationClassWithAbstractMembersAdaptation(AssociationClass source)
			throws NotAnAssociationClassException, NotAnAssociationWithAnAbstractMemberException {
		
		if(!Associations.isAssociationClass(source))
			throw new NotAnAssociationClassException(source.getName() + 
					" is not an association class");
		
		if(!Associations.hasAnAbstractMember(source))
			throw new NotAnAssociationWithAnAbstractMemberException(source.getName() 
					+ " doesn't have any abstract members");
		
		apply(source);
	}
	
	/**
	 * Clones the provided enqueued intermediary association class.
	 */
	@Override
	protected AssociationClass cloneIntermediaryRelationship(Association intermediaryAssociation) {
		AssociationClass intermediateAssociationClass = (AssociationClass) intermediaryAssociation;
		AssociationClass newOwnedAssociationClass = UMLFactory.eINSTANCE.createAssociationClass();
		Property newMemberEnd;
		
		for (Property memberEnd: intermediateAssociationClass.getMemberEnds()) {
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociationClass, newMemberEnd, memberEnd.isNavigable());
		}
		
		intermediateAssociationClass.getOwnedAttributes()
		.stream()
		.forEach(attribute -> 
			newOwnedAssociationClass.createOwnedAttribute(attribute.getName(), attribute.getType()));
		
		return newOwnedAssociationClass;
	}
}

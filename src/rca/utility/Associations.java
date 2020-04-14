package rca.utility;

import java.util.stream.Collectors;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * an Associations concrete class encapsulating some reusable utility association functions.
 * 
 * @author Bachar Rima
 * @see Association
 */
public class Associations {
	
	/**
	 * Checks if an association is bidirectional.
	 * @param association the association to check.
	 * @return true if the association is bidirectional, false otherwise.
	 */
	public static boolean isBidirectional(Association association) {
		return association.getMemberEnds()
		.stream()
		.filter(memberEnd -> memberEnd.isNavigable())
		.collect(Collectors.toList())
		.size() == 2;
	}
	
	public static boolean isNary(Association association) {
		return !(association.isBinary() || isAssociationClass(association));
	}
	
	public static boolean isAssociationClass(Association association) {
		return association instanceof AssociationClass;
	}
	
	/**
	 * Adapts a non general association (e.g. aggregation, composition, dependency) into a general association
	 * by getting rid of its aggregation kind.
	 * @param association the non general association to adapt.
	 * @param kind the aggregation kind of the non general association.
	 * @return a general association obtained from the non general association.
	 * @see AggregationKind
	 */
	public static Association toGeneralAssociation(Association association, AggregationKind kind) {
		Property kindEnd = null;
		
		for (Property memberEnd: association.getMemberEnds()) {
			if(memberEnd.getAggregation().equals(kind)) {
				kindEnd = memberEnd;
				break;
			}
		}
		
		kindEnd.setAggregation(AggregationKind.NONE_LITERAL);
		
		return association;
	}
	
	/**
	 * creates a binary unidirectional association from the ends of a binary bidirectional association 
	 * while specifying its navigable and non-navigable ends.
	 * @param navigableEnd the navigable end in the created unidirectional association.
	 * @param nonNavigableEnd the non navigable end in the created unidirectional association.
	 * @return a unidirectional binary association obtained from the ends of a bidirectional association.
	 * and the specified navigable and non-navigable ends.
	 */
	public static Association createUniDirectionalAssociation(Property navigableEnd, Property nonNavigableEnd) {
		Association unidirectionalAssociation = UMLFactory.eINSTANCE.createAssociation();
		
		createNavigableMemberEnd(unidirectionalAssociation, navigableEnd, nonNavigableEnd);
		createNonNavigableMemberEnd(unidirectionalAssociation, nonNavigableEnd);
		
		return unidirectionalAssociation;
	}
	
	public static Association createBinaryAssociation(Property firstEnd, Property secondEnd) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		
		if (firstEnd.isNavigable())
			createNavigableMemberEnd(association, firstEnd, secondEnd);
		
		else
			createNonNavigableMemberEnd(association, firstEnd);
		
		if (secondEnd.isNavigable())
			createNavigableMemberEnd(association, secondEnd, firstEnd);
		
		else
			createNonNavigableMemberEnd(association, secondEnd);
		
		return association;
	}
	
	public static Property copyMemberEnd(Property memberEnd) {
		Property newMemberEnd = UMLFactory.eINSTANCE.createProperty();
		
		newMemberEnd.setIsNavigable(memberEnd.isNavigable());
		newMemberEnd.setAggregation(memberEnd.getAggregation());
		newMemberEnd.setName(memberEnd.getName());
		newMemberEnd.setLower(memberEnd.getLower());
		newMemberEnd.setUpper(memberEnd.getUpper());
		newMemberEnd.setType(memberEnd.getType());
		
		return newMemberEnd;
	}
	
	public static void becomeMemberEnd(Property memberEnd, Association newContainingAssociation) {
		memberEnd.setAssociation(newContainingAssociation);
	}

	public static Property createNonNavigableMemberEnd(Association association, Property nonNavigableEnd) {
		Property newNonNavigableEnd;
		newNonNavigableEnd = UMLFactory.eINSTANCE.createProperty();
		newNonNavigableEnd.setOwningAssociation(association);
		newNonNavigableEnd.setIsNavigable(false);
		newNonNavigableEnd.setAggregation(nonNavigableEnd.getAggregation());
		newNonNavigableEnd.setName(nonNavigableEnd.getName());
		newNonNavigableEnd.setLower(nonNavigableEnd.getLower());
		newNonNavigableEnd.setUpper(nonNavigableEnd.getUpper());
		newNonNavigableEnd.setType(nonNavigableEnd.getType());
		
		return newNonNavigableEnd;
	}

	public static void createNavigableMemberEnd(Association association, Property navigableEnd, Property otherEnd) {
		Property newNavigableEnd;
		Class otherEndClass = (Class) otherEnd.getType();
		otherEndClass.createOwnedAttribute(navigableEnd.getName(), navigableEnd.getType());
		
		newNavigableEnd = otherEndClass.getOwnedAttribute(navigableEnd.getName(), navigableEnd.getType());
		newNavigableEnd.setAssociation(association);
		newNavigableEnd.setIsNavigable(true);
		newNavigableEnd.setAggregation(navigableEnd.getAggregation());
		newNavigableEnd.setLower(navigableEnd.getLower());
		newNavigableEnd.setUpper(navigableEnd.getUpper());
	}
}
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
	 * Checks if a binary association is unidirectional.
	 * @param association the binary association to check.
	 * @return true if the binary association is unidirectional, false otherwise.
	 */
	public static boolean isUnidirectional(Association association) {
		if (association.isBinary())
			return association.getMemberEnds()
			.stream()
			.filter(memberEnd -> memberEnd.isNavigable())
			.collect(Collectors.toList())
			.size() == 1;
		else return false;
	}
	
	/**
	 * Checks if a binary association is bidirectional.
	 * @param association the binary association to check.
	 * @return true if the binary association is bidirectional, false otherwise.
	 */
	public static boolean isBidirectional(Association association) {
		if (association.isBinary())
			return association.getMemberEnds()
			.stream()
			.filter(memberEnd -> memberEnd.isNavigable())
			.collect(Collectors.toList())
			.size() == 2;
		else return false;
	}
	
	/**
	 * Checks if a binary association is an aggregation.
	 * @param association the binary association to check.
	 * @return true if the binary association is an aggregation, false otherwise.
	 */
	public static boolean isAggregation(Association association) {
		if (association.isBinary())
			return !association.getMemberEnds()
			.stream()
			.filter(memberEnd -> memberEnd.getAggregation() == AggregationKind.SHARED_LITERAL)
			.collect(Collectors.toList())
			.isEmpty();
		else return false;
	}
	
	/**
	 * Checks if a binary association is a composition.
	 * @param association the binary association to check.
	 * @return true if the binary association is a composition, false otherwise.
	 */
	public static boolean isComposition(Association association) {
		if (association.isBinary())
			return !association.getMemberEnds()
			.stream()
			.filter(memberEnd -> memberEnd.getAggregation() == AggregationKind.COMPOSITE_LITERAL)
			.collect(Collectors.toList())
			.isEmpty();
		else return false;
	}
	
	/**
	 * Checks if an association is an association class.
	 * @param association the association to check.
	 * @return true if the association is an association class, false otherwise.
	 */
	public static boolean isAssociationClass(Association association) {
		return association instanceof AssociationClass;
	}
	
	/**
	 * Checks if an association is an n-ary association.
	 * @param association the association to check.
	 * @return true if the association is n-ary, false otherwise.
	 */
	public static boolean isNary(Association association) {
		return !(association.isBinary() || isAssociationClass(association));
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
	
	public static Association cloneIntoBinaryAssociation(Property firstEnd, Property secondEnd) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		
		Property newFirstEnd = cloneMemberEnd(firstEnd);
		adaptMemberEndOwnership(association, newFirstEnd, firstEnd.isNavigable());
		
		Property newSecondEnd = cloneMemberEnd(secondEnd);
		adaptMemberEndOwnership(association, newSecondEnd, secondEnd.isNavigable());
		
		return association;
	}
	
	/**
	 * creates a new binary unidirectional association from the ends of an existing association 
	 * while specifying its navigable and non-navigable ends.
	 * @param navigableEnd the navigable end in the created unidirectional association.
	 * @param nonNavigableEnd the non navigable end in the created unidirectional association.
	 * @return a unidirectional binary association obtained from an existing association,
	 * while specifying its navigable and non-navigable ends.
	 */
	public static Association cloneIntoUnidirectionalAssociation(Property navigableEnd, Property nonNavigableEnd) {
		Association unidirectionalAssociation = UMLFactory.eINSTANCE.createAssociation();
		
		Property newNavigableEnd = cloneMemberEnd(navigableEnd);
		adaptMemberEndOwnership(
				unidirectionalAssociation, newNavigableEnd, true);
		
		Property newNonNavigableEnd = cloneMemberEnd(nonNavigableEnd);
		adaptMemberEndOwnership(
				unidirectionalAssociation, newNonNavigableEnd, false);
		
		return unidirectionalAssociation;
	}
	
	public static Property cloneMemberEnd(Property memberEnd) {
		Property newMemberEnd = UMLFactory.eINSTANCE.createProperty();
		
		newMemberEnd.setAggregation(memberEnd.getAggregation());
		newMemberEnd.setName(memberEnd.getName());
		newMemberEnd.setLower(memberEnd.getLower());
		newMemberEnd.setUpper(memberEnd.getUpper());
		newMemberEnd.setType(memberEnd.getType());
		
		return newMemberEnd;
	}
	
	public static void adaptMemberEndOwnership(Association association,
			Property clone, boolean isNavigable) {
		if (isNavigable)
			clone.setAssociation(association);
		else
			clone.setOwningAssociation(association);
		clone.setIsNavigable(isNavigable);
	}

	public static Property cloneNonNavigableMemberEnd(Association association, Property nonNavigableEnd) {
		Property newNonNavigableEnd = UMLFactory.eINSTANCE.createProperty();
		newNonNavigableEnd.setOwningAssociation(association);
		newNonNavigableEnd.setIsNavigable(false);
		newNonNavigableEnd.setAggregation(nonNavigableEnd.getAggregation());
		newNonNavigableEnd.setName(nonNavigableEnd.getName());
		newNonNavigableEnd.setLower(nonNavigableEnd.getLower());
		newNonNavigableEnd.setUpper(nonNavigableEnd.getUpper());
		newNonNavigableEnd.setType(nonNavigableEnd.getType());
		
		return newNonNavigableEnd;
	}

	public static void cloneNavigableMemberEndForBinaryAssociations(Association association, Property navigableEnd, Property otherEnd) {
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
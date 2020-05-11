package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.adaptation.AbstractRelationshipWithAbstractMembersAdaptation;
import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;

/**
 * an AssociationWithAbstractMembersAdaptation concrete class that is used to adapt a UML
 * association having abstract members by moving down the association into each non abstract child 
 * of each of its abstract members.<br><br>
 * 
 * The adaptation consists of creating a list of target associations having the 
 * same owning package as the source association to adapt, such that the members of each 
 * target association consist of the original non abstract members of the source association 
 * and a non abstract child of the original abstract member of the source association.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class AssociationWithAbstractMembersAdaptation extends AbstractRelationshipWithAbstractMembersAdaptation<Association> {
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty association with abstract members adaptation
	 */
	public AssociationWithAbstractMembersAdaptation() {}
	
	/**
	 * Creates an association with abstract members adaptation having source as its source
	 * association to adapt, then applies the adaptation to obtain the list of target
	 * associations with no abstract members, such that the members of each 
	 * target association consist of the original non abstract members of the source association 
	 * and a non abstract child of the original abstract member of the source association.
	 * It then cleans the post-adaptation residues.
	 * @param source the source association to adapt
	 * @throws NotAnAssociationWithAnAbstractMemberException if the provided source association 
	 * doesn't have abstract members
	 */
	public AssociationWithAbstractMembersAdaptation(Association source) 
			throws NotAnAssociationWithAnAbstractMemberException {
		
		if(!Associations.hasAnAbstractMember(source))
			throw new NotAnAssociationWithAnAbstractMemberException(source.getName() 
					+ " doesn't have any abstract members");
		
		apply(source);
	}

	/* METHODS */
	/**
	 * Creates a new empty list of associations 
	 */
	@Override
	protected List<Association> createNewRelationshipEmptyList() {
		return new ArrayList<>();
	}

	/**
	 * Initializes the the content of the abstract members dictionary
	 * of the source association with abstract members to adapt, by
	 * mapping each of its abstract members to the set of its non abstract
	 * subclasses.
	 */
	@Override
	protected void initSourceRelationshipAbstractMembersDictionary() {
		source.getEndTypes()
		.stream()
		.map(type -> (Class) type)
		.filter(cls -> cls.isAbstract())
		.forEach(cls -> {
			abstractMembersDictionary.put(cls,
				Classes.getAllSubclasses(cls)
				.stream()
				.filter(subClass -> !subClass.isAbstract())
				.collect(Collectors.toSet()));
		});
	}

	/**
	 * Checks whether the provided association has an abstract member
	 */
	@Override
	protected boolean hasAnAbstractMember(Association association) {
		return Associations.hasAnAbstractMember(association);
	}

	/**
	 * Returns the first abstract member of the provided association
	 */
	@Override
	protected Class getFirstAbstractMember(Association association) {
		return Associations.getFirstAbstractMember(association);
	}
	
	/**
	 * Clones the provided enqueued intermediary association.
	 */
	@Override
	protected Association cloneIntermediaryRelationship(Association intermediaryAssociation) {
		Association newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
		Property newMemberEnd;
		
		for (Property memberEnd: intermediaryAssociation.getMemberEnds()) {
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
		}
		
		return newOwnedAssociation;
	}
	
	/**
	 * Adapts the first abstract member of the provided intermediary association 
	 * by replacing it with one of its non abstract subclasses.
	 */
	@Override
	protected Association adaptIntermediaryRelationship(Association intermediaryAssociation, Class abstractMember, Class subClass) {
		Property abstractMemberEnd = Associations.getFirstMemberEndHavingType(
				intermediaryAssociation, abstractMember);
		
		abstractMemberEnd.setName(Strings.decapitalize(subClass.getName()));
		abstractMemberEnd.setType(subClass);
		
		return intermediaryAssociation;
	}

	/**
	 * Sets the name and the owning package for each target association
	 * in the provided list of target associations.
	 * The owning package is the same one owning the source association
	 * with abstract members to adapt, whereas the name conforms to 
	 * the following convention &lt;sourceAssociationName&gt;-&lt;newNonAbstractMemberName&gt;
	 */
	@Override
	protected void initTargetRelationshipsNamesAndOwnership(List<Association> newOwnedAssociations) {
		newOwnedAssociations
		.stream()
		.forEach(newAssociation -> {
			newAssociation.setPackage(source.getPackage());
			String newAssociationName = source.getName();
			
			for (Property memberEnd: newAssociation.getMemberEnds())
				if (!source.getEndTypes().contains(memberEnd.getType())
						|| Associations.associatesTypesInSameGeneralization(source))
					newAssociationName += "-" + memberEnd.getType().getName();
			
			newAssociation.setName(newAssociationName);
		});
	}
}

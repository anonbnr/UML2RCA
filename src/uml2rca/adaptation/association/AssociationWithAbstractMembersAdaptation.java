package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;

public class AssociationWithAbstractMembersAdaptation extends AbstractAdaptation<Association, List<Association>> {
	
	/* ATTRIBUTES */
	protected Map<Class, Set<Class>> abstractMembersDictionary;
	protected Queue<Association> newAssociationsQueue;
	protected List<Association> associationsToClean;
	
	/* CONSTRUCTOR */
	public AssociationWithAbstractMembersAdaptation(Association source) 
			throws NotAnAssociationWithAnAbstractMemberException {
		
		if(!Associations.hasAnAbstractMember(source))
			throw new NotAnAssociationWithAnAbstractMemberException(source.getName() 
					+ " doesn't have any abstract members");
		
		this.setSource(source);
		abstractMembersDictionary = new Hashtable<>();
		newAssociationsQueue = new LinkedList<>();
		associationsToClean = new ArrayList<>();
		this.setTarget(transform(source));
		this.postTransformationClean();
	}

	/* METHODS */
	@Override
	public List<Association> transform(Association source) {
		List<Association> newOwnedAssociations = new ArrayList<>();
		initTargetAssociations(newOwnedAssociations);
		
		return newOwnedAssociations;
	}	
	
	protected List<Association> initTargetAssociations(List<Association> newOwnedAssociations) {
		initAbstractMemberEndsDictionary();
		
		newAssociationsQueue.add(source);
		
		while(!newAssociationsQueue.isEmpty()) {
			final Association currentAssociation = newAssociationsQueue.element();
			
			if (Associations.hasAnAbstractMember(currentAssociation)) {
				Class abstractMember = Associations.getFirstAbstractMember(
						currentAssociation);
				
				abstractMembersDictionary.get(abstractMember)
				.stream()
				.forEach(subClass -> {
					Association clonedAssociation = cloneSourceAssociationAndAdaptFirstAbstractMemberEnd(
							currentAssociation, abstractMember, subClass);
					newAssociationsQueue.add(clonedAssociation);
				});
				
				associationsToClean.add(currentAssociation);
			}
			
			else
				newOwnedAssociations.add(currentAssociation);
			
			newAssociationsQueue.remove();
		}
		
		initTargetAssociationsNamesAndOwnership(newOwnedAssociations);
		
		return newOwnedAssociations;
	}
	
	protected void initAbstractMemberEndsDictionary() {
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
	
	protected Association initTargetAssociation(Association currentAssociation) {
		Association newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
		Property newMemberEnd;
		
		for (Property memberEnd: currentAssociation.getMemberEnds()) {
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
		}
		
		return newOwnedAssociation;
	}
	
	protected Association cloneSourceAssociationAndAdaptFirstAbstractMemberEnd(
			Association currentAssociation, Class abstractMember,
			Class subClass) {
		
		Association clonedAssociation = initTargetAssociation(currentAssociation);
		Property abstractMemberEnd = Associations.getFirstMemberEndHavingType(
				clonedAssociation, abstractMember);
		
		abstractMemberEnd.setName(Strings.decapitalize(subClass.getName()));
		abstractMemberEnd.setType(subClass);
		
		return clonedAssociation;
	}
	
	protected void initTargetAssociationsNamesAndOwnership(List<Association> newOwnedAssociations) {
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

	protected void postTransformationClean() {
		associationsToClean
		.stream()
		.forEach(Association::destroy);
	}
}

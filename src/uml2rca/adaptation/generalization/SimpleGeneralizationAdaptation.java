package uml2rca.adaptation.generalization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.java.uml2.uml.extensions.utility.NamedElements;
import uml2rca.management.EcoreModelManager;

public class SimpleGeneralizationAdaptation extends AbstractAdaptation<Class, Class> {
	
	/* ATTRIBUTES */
	protected List<Association> associationsToClean;
	protected List<Dependency> dependenciesToClean;
	protected List<Class> superClasses;
	protected List<Class> subClasses;
	
	/* CONSTRUCTOR */
	public SimpleGeneralizationAdaptation(Class leaf, Class choice) 
			throws NotALeafInGeneralizationHierarchyException,
			NotAValidLevelForGeneralizationAdaptationException {
		
		if (!Classes.isLeafInGeneralizationStructure(leaf))
			throw new NotALeafInGeneralizationHierarchyException(leaf.getName() 
					+ " is not a leaf in a generalization hierarchy");
		
		if (!(leaf == choice || Classes.getAllSuperClasses(leaf).contains(choice)))
			throw new NotAValidLevelForGeneralizationAdaptationException(choice.getName()
					+ " is neither " + leaf.getName() + " nor one of its superclasses");
		
		associationsToClean = new ArrayList<>();
		dependenciesToClean = new ArrayList<>();
		superClasses = Classes.getAllSuperClasses(choice);
		subClasses = Classes.getAllSubclasses(choice);
		this.setSource(choice);
		this.setTarget(this.transform(choice));
		postTransformationClean();
	}

	/* METHODS */
	public List<Class> getSuperClasses() {
		return superClasses;
	}

	public List<Class> getSubClasses() {
		return subClasses;
	}
	
	// implementation of the IAdaptation interface
	@Override
	public Class transform(Class source) {
		Class target = initTargetClass(source);
		
		// add owned, inherited, and specializing attributes
		initAllTargetClassAttributes(source, target);
		
		// add owned, inherited, and specializing associations
		initAllTargetClassAssociations(source, target);
		
		// add owned, inherited, and specializing dependencies
		initAllTargetClassDependencies(source, target);
		
		return target;
	}

	protected Class initTargetClass(Class source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		
		// initialize the class' name and package
		cls.setName(source.getName());
		cls.setPackage(source.getPackage());
		return cls;
	}
	
	protected void initTargetClassAttributes(Class source, Class target) {
		for (Property ownedAttribute: source.getOwnedAttributes())
			target.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType());
	}

	protected void initAllTargetClassAttributes(Class source, Class target) {
		/* owned attributes */
		initTargetClassAttributes(source, target);
		
		/* inherited attributes (superclasses) */
		for (Class superClass: superClasses)
			initTargetClassAttributes(superClass, target);
		
		/* specializing attributes (subclasses) */
		for (Class subClass: subClasses) {
			initTargetClassAttributes(subClass, target);
			
			target.createOwnedAttribute(
					Strings.decapitalize(subClass.getName()), 
					EcoreModelManager.UML_PRIMITIVE_TYPES_LIBRARY.getOwnedType("Boolean"));
		}
	}
	
	protected void initTargetClassAssociations(Class owner, Class target,
			Association ownedAssociation) {
		Association newOwnedAssociation = null;
		
		if (Associations.isAssociationClass(ownedAssociation))
			newOwnedAssociation = initTargetClassAssociationClasses((AssociationClass)ownedAssociation);			
		
		else
			newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
		
		newOwnedAssociation.setPackage(ownedAssociation.getPackage());
		newOwnedAssociation.setName(ownedAssociation.getName());
		
		if (Associations.isUnary(ownedAssociation))
			initTargetClassUnaryAssociations(owner, target, ownedAssociation, newOwnedAssociation);
		
		else
			initTargetClassNonUnaryAssociations(owner, target, ownedAssociation, newOwnedAssociation);	
		
		if (owner == source || subClasses.contains(owner))
			associationsToClean.add(ownedAssociation);
	}

	protected void initTargetClassUnaryAssociations(Class owner, Class target, Association ownedAssociation,
			Association newOwnedAssociation) {
		Property newMemberEnd;
		
		for (Property memberEnd: ownedAssociation.getMemberEnds()) {
			
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
			
			if (owner == source)
				newMemberEnd.setType(target);
			else if (memberEnd == ownedAssociation.getMemberEnds().get(0)) {
				newMemberEnd.setName(Strings.decapitalize(source.getName()));
				newMemberEnd.setType(target);
				newOwnedAssociation.setName(ownedAssociation.getName() + "-" + target.getName());
			}
		}
	}
	
	protected void initTargetClassNonUnaryAssociations(Class owner, Class target, Association ownedAssociation,
			Association newOwnedAssociation) {
		Property newMemberEnd;
		
		for (Property memberEnd: ownedAssociation.getMemberEnds()) {
			
			newMemberEnd = Associations.cloneMemberEnd(memberEnd);
			Associations.adaptMemberEndOwnership(
					newOwnedAssociation, newMemberEnd, memberEnd.isNavigable());
			
			if (memberEnd.getType() == owner && owner == source)
				newMemberEnd.setType(target);
			
			if (memberEnd.getType() == owner && owner != source) {
				newMemberEnd.setName(Strings.decapitalize(source.getName()));
				newMemberEnd.setType(target);
				newOwnedAssociation.setName(ownedAssociation.getName() + "-" + target.getName());
			}
		}
	}

	protected AssociationClass initTargetClassAssociationClasses(AssociationClass ownedAssociation) {
		AssociationClass newOwnedAssociation = UMLFactory.eINSTANCE.createAssociationClass();
		
		ownedAssociation.getOwnedAttributes()
		.stream()
		.filter(attribute -> 
			!ownedAssociation.getMemberEnds()
				.stream()
				.map(Property::getName)
				.collect(Collectors.toList())
				.contains(attribute.getName()))
		.forEach(attribute -> 
			newOwnedAssociation.createOwnedAttribute(attribute.getName(), attribute.getType()));
		
		return newOwnedAssociation;
	}
	
	protected void initAllTargetClassAssociations(Class source, Class target) {
		/* owned associations */
		for (Association ownedAssociation: source.getAssociations())
			initTargetClassAssociations(source, target, ownedAssociation);
		
		/* inherited associations (superclasses) */
		for (Class superClass: superClasses)
			for (Association ownedAssociation: superClass.getAssociations())
				initTargetClassAssociations(superClass, target, ownedAssociation);
		
		/* specializing associations (subclasses) */
		for (Class subClass: subClasses)
			for (Association ownedAssociation: subClass.getAssociations())
				initTargetClassAssociations(subClass, target, ownedAssociation);
	}
	
	protected void initTargetClassClientDependencies(Class dependingClient, Class target, Dependency dependency) {
		Dependency newDependency = UMLFactory.eINSTANCE.createDependency();
		dependency.getNearestPackage().getPackagedElements().add(newDependency);
		
		if (dependingClient != source)
			newDependency.setName(dependency.getName() + "-" + target.getName());
		else
			newDependency.setName(dependency.getName());
		
		for (NamedElement client: dependency.getClients()) {
			
			if (client == dependingClient)
				newDependency.getClients().add(target);
			else
				newDependency.getClients().add(client);
			
			for (NamedElement supplier: dependency.getSuppliers())
				newDependency.getSuppliers().add(supplier);
		}
		
		if (dependingClient == source || subClasses.contains(dependingClient))
			dependenciesToClean.add(dependency);
	}
	
	protected void initTargetClassSupplierDependencies(Class providingSupplier, Class target, Dependency dependency) {
		Dependency newDependency = UMLFactory.eINSTANCE.createDependency();
		dependency.getNearestPackage().getPackagedElements().add(newDependency);
		
		if (providingSupplier != source)
			newDependency.setName(dependency.getName() + "-" + target.getName());
		else
			newDependency.setName(dependency.getName());
		
		for (NamedElement client: dependency.getClients()) {
			newDependency.getClients().add(client);
			
			for (NamedElement supplier: dependency.getSuppliers()) {
				if (supplier == providingSupplier)
					newDependency.getSuppliers().add(target);
				else
					newDependency.getSuppliers().add(supplier);
			}
		}
		
		if (providingSupplier == source || subClasses.contains(providingSupplier))
			dependenciesToClean.add(dependency);
	}
	
	protected void initAllTargetClassDependencies(Class source, Class target) {
		/* client dependencies */
		for (Dependency dependency: source.getClientDependencies())
			initTargetClassClientDependencies(source, target, dependency);
		
		/* supplier dependencies */
		for (Dependency dependency: NamedElements.getSupplierDependencies(source))
			initTargetClassSupplierDependencies(source, target, dependency);
		
		/* inherited client dependencies (superclasses) */
		for (Class superClass: superClasses)
			for (Dependency dependency: superClass.getClientDependencies())
				initTargetClassClientDependencies(superClass, target, dependency);
		
		/* inherited supplier dependencies (superclasses) */
		for (Class superClass: superClasses)
			for (Dependency dependency: NamedElements.getSupplierDependencies(superClass))
				initTargetClassSupplierDependencies(superClass, target, dependency);
		
		/* specializing client dependencies (subclasses) */
		for (Class subClass: subClasses)
			for (Dependency dependency: subClass.getClientDependencies())
				initTargetClassClientDependencies(subClass, target, dependency);
		
		/* specializing supplier dependencies (subclasses) */
		for (Class subClass: subClasses)
			for (Dependency dependency: NamedElements.getSupplierDependencies(subClass))
				initTargetClassSupplierDependencies(subClass, target, dependency);
	}

	protected void postTransformationClean() {
		// remove source's owned associations and its subclasses' associations
		associationsToClean.stream().forEach(Association::destroy);
		
		// remove source's dependencies and its subclasses' dependencies
		dependenciesToClean.stream().forEach(Dependency::destroy);
		
		// remove source and its collapsed subclasses
		Classes.getAllSubclasses(source)
		.stream().forEach(Class::destroy);
		
		source.destroy();
	}
}

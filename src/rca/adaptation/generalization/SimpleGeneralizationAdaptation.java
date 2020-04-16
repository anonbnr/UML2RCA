package rca.adaptation.generalization;

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
import rca.utility.Associations;
import rca.utility.Classes;
import rca.utility.NamedElements;
import utility.Strings;

public class SimpleGeneralizationAdaptation extends AbstractAdaptation<Class, Class> {
	
	/* ATTRIBUTES */
	private List<Association> associationsToClean;
	private List<Dependency> dependenciesToClean;
	private List<Class> superClasses;
	private List<Class> subClasses;
	
	/* CONSTRUCTOR */
	public SimpleGeneralizationAdaptation(Class source) {
		associationsToClean = new ArrayList<>();
		dependenciesToClean = new ArrayList<>();
		superClasses = Classes.getAllSuperClasses(source);
		subClasses = Classes.getAllSubclasses(source);
		this.setSource(source);
		this.setTarget(this.transform(source));
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

	private Class initTargetClass(Class source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		
		// initialize the class' name and package
		cls.setName(source.getName());
		cls.setPackage(source.getPackage());
		return cls;
	}
	
	private void initTargetClassAttributes(Class source, Class target) {
		for (Property ownedAttribute: source.getOwnedAttributes())
			target.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType());
	}

	private void initAllTargetClassAttributes(Class source, Class target) {
		/* owned attributes */
		initTargetClassAttributes(source, target);
		
		/* inherited attributes (superclasses) */
		for (Class superClass: superClasses)
			initTargetClassAttributes(superClass, target);
		
		/* specializing attributes (subclasses) */
		for (Class subClass: subClasses) {
			initTargetClassAttributes(subClass, target);
			
			/* boolean attribute for each subclass */
//			cls.createOwnedAttribute(subClass.getName(), UMLPackage.LITERAL_BOOLEAN);
		}
	}
	
	private void initTargetClassAssociations(Class owner, Class target,
			Association ownedAssociation) {
		Association newOwnedAssociation = null;
		Property newMemberEnd = null;
		
		if (Associations.isAssociationClass(ownedAssociation))
			newOwnedAssociation = initTargetClassAssociationClasses((AssociationClass)ownedAssociation);			
		
		else
			newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
		
		newOwnedAssociation.setPackage(ownedAssociation.getPackage());
		newOwnedAssociation.setName(ownedAssociation.getName());
		
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
		
		if (owner == source || subClasses.contains(owner))
			associationsToClean.add(ownedAssociation);
	}

	private AssociationClass initTargetClassAssociationClasses(AssociationClass ownedAssociation) {
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
	
	private void initAllTargetClassAssociations(Class source, Class target) {
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
	
	private void initTargetClassClientDependencies(Class dependingClient, Class target, Dependency dependency) {
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
	
	private void initTargetClassSupplierDependencies(Class providingSupplier, Class target, Dependency dependency) {
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
	
	private void initAllTargetClassDependencies(Class source, Class target) {
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

	private void postTransformationClean() {
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

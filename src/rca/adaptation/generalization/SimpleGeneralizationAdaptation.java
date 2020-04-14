package rca.adaptation.generalization;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import rca.utility.Associations;

public class SimpleGeneralizationAdaptation extends AbstractAdaptation<Class, Class> {
	
	/* CONSTRUCTOR */
	public SimpleGeneralizationAdaptation(Class source) {
		super(source);
	}

	/* METHODS */
	// implementation of the IAdaptation interface
	@Override
	public Class transform(Class source) {
		// start by creating the new class
		Class cls = UMLFactory.eINSTANCE.createClass();
		
		// initialize the class' name and package
		cls.setName(source.getName());
		cls.setPackage(source.getPackage());
		
		// get superclasses
		EList<Class> superClasses = getAllSuperClasses(source);
		
		// get subclasses
		EList<Class> subClasses = getAllSubclasses(source);
		
		// add attributes
		/* attributes owned by C_ch */
		for (Property ownedAttribute: source.getOwnedAttributes())
			cls.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType());
		
		/* attributes owned by C_ch's superclasses */
		for (Class superClass: superClasses) {
			for (Property superClassOwnedAttribute: superClass.getOwnedAttributes())
				cls.createOwnedAttribute(superClassOwnedAttribute.getName(), superClassOwnedAttribute.getType());
		}
		
		/* attributes owned by C_ch's subclasses */
		for (Class subClass: subClasses) {
			for (Property subClassOwnedAttribute: subClass.getOwnedAttributes())
				cls.createOwnedAttribute(subClassOwnedAttribute.getName(), subClassOwnedAttribute.getType());
			
			/* boolean attribute for each subclass */
//			cls.createOwnedAttribute(subClass.getName(), UMLPackage.LITERAL_BOOLEAN);
		}
		
		// add associations
		/* associations owned by C_ch */
		for (Association ownedAssociation: source.getAssociations()) {
			Association newOwnedAssociation = null;
			Property clsMemberEnd = null;
			
			if (Associations.isAssociationClass(ownedAssociation))
				newOwnedAssociation = UMLFactory.eINSTANCE.createAssociationClass();
			
			else
				newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
			
			for (Property memberEnd: ownedAssociation.getMemberEnds()) {
				if (memberEnd.getType() != source)
					Associations.becomeMemberEnd(
							Associations.copyMemberEnd(memberEnd),
							newOwnedAssociation
					);
				else {
					clsMemberEnd = Associations.copyMemberEnd(memberEnd);
					clsMemberEnd.setType(cls);
					Associations.becomeMemberEnd(clsMemberEnd, newOwnedAssociation);
				}
			}
		}
		
		/* associations owned by C_ch's superclasses */
		for (Class superClass: superClasses) {
			for (Association ownedAssociation: superClass.getAssociations()) {
				Association newOwnedAssociation = null;
				Property clsMemberEnd = null;
				
				if (Associations.isAssociationClass(ownedAssociation))
					newOwnedAssociation = UMLFactory.eINSTANCE.createAssociationClass();
				
				else
					newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
				
				for (Property memberEnd: ownedAssociation.getMemberEnds()) {
					if (memberEnd.getType() != source)
						Associations.becomeMemberEnd(
								Associations.copyMemberEnd(memberEnd),
								newOwnedAssociation
						);
					else {
						clsMemberEnd = Associations.copyMemberEnd(memberEnd);
						clsMemberEnd.setType(cls);
						Associations.becomeMemberEnd(clsMemberEnd, newOwnedAssociation);
					}
				}
			}
		}
		
		/* associations owned by C_ch's subclasses */
		for (Class subClass: subClasses) {
			for (Association ownedAssociation: subClass.getAssociations()) {
				Association newOwnedAssociation = null;
				Property clsMemberEnd = null;
				
				if (Associations.isAssociationClass(ownedAssociation))
					newOwnedAssociation = UMLFactory.eINSTANCE.createAssociationClass();
				
				else
					newOwnedAssociation = UMLFactory.eINSTANCE.createAssociation();
				
				for (Property memberEnd: ownedAssociation.getMemberEnds()) {
					if (memberEnd.getType() != source)
						Associations.becomeMemberEnd(
								Associations.copyMemberEnd(memberEnd),
								newOwnedAssociation
						);
					else {
						clsMemberEnd = Associations.copyMemberEnd(memberEnd);
						clsMemberEnd.setType(cls);
						Associations.becomeMemberEnd(clsMemberEnd, newOwnedAssociation);
					}
				}
			}
		}
		
		// remove source and its collapsed subclasses
		subClasses.stream().forEach(Class::destroy);
		source.destroy();
		
		return cls;
	}
	
	public static EList<Package> getAllNestedPackages(Package pckg) {
		EList<Package> allNestedPackages = new BasicEList<>();
		
		for (Package nestedPackage: pckg.getNestedPackages()) {
			allNestedPackages.add(nestedPackage);
			allNestedPackages.addAll(getAllNestedPackages(nestedPackage));
		}
		
		return allNestedPackages;
	}
	
	public static EList<Package> getAllNestedPackages(Model model) {
		EList<Package> allNestedPackages = new BasicEList<>();
		
		for (Package nestedPackage: model.getNestedPackages()) {
			allNestedPackages.add(nestedPackage);
			allNestedPackages.addAll(getAllNestedPackages(nestedPackage));
		}
		
		return allNestedPackages;
	}
	
	public static EList<Class> getAllSuperClasses(Class source) {
		EList<Class> superClasses = new BasicEList<>();
		
		for (Class superClass: source.getSuperClasses()) {
			superClasses.add(superClass);
			superClasses.addAll(getAllSuperClasses(superClass));
		}
		
		return superClasses;
	}
	
	public static EList<Class> getAllSubclasses(Class source) {
		EList<Class> subClasses = new BasicEList<>();
		
		Model model = source.getModel();
		for (Package nestedPackage: getAllNestedPackages(model)) {
			for (Type ownedType: nestedPackage.getOwnedTypes()) {
				if (ownedType instanceof Class) {
					Class ownedClass = (Class) ownedType;
					if (ownedClass.getSuperClasses().contains(source)) {
						subClasses.add(ownedClass);
						subClasses.addAll(getAllSubclasses(ownedClass));
					}
				}
			}
		}
		
		return subClasses;
	}
}

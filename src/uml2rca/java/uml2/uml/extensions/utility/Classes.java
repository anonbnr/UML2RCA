package uml2rca.java.uml2.uml.extensions.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

public class Classes {

	public static List<Class> getAllSuperClasses(Class cls) {
		List<Class> superClasses = new ArrayList<>();
		
		for (Class superClass: cls.getSuperClasses()) {
			superClasses.add(superClass);
			superClasses.addAll(getAllSuperClasses(superClass));
		}
		
		superClasses = new ArrayList<>(
				new HashSet<>(superClasses));
		return superClasses;
	}

	public static List<Class> getAllSubclasses(Class cls) {
		List<Class> subClasses = new ArrayList<>();
		
		Model model = cls.getModel();
		for (Package nestedPackage: Models.getAllNestedPackages(model)) {
			for (Type ownedType: nestedPackage.getOwnedTypes()) {
				if (ownedType instanceof Class) {
					Class ownedClass = (Class) ownedType;
					if (ownedClass.getSuperClasses().contains(cls)) {
						subClasses.add(ownedClass);
						subClasses.addAll(getAllSubclasses(ownedClass));
					}
				}
			}
		}
		
		subClasses = new ArrayList<>(
				new HashSet<>(subClasses));
		return subClasses;
	}
	
	public static boolean hasAttribute(Class cls, String name, Type type) {
		return cls.getOwnedAttributes()
				.stream()
				.anyMatch(attribute -> 
					attribute.getName().equals(name) 
					&& attribute.getType().getName().equals(type.getName()));
	}
	
	public static boolean hasAssociation(Class cls, String associationName, 
			List<MutablePair<String, Type>> others) {
		return cls.getAssociations()
				.stream()
				.anyMatch(association -> 
					association.getName().equals(associationName) 
					&& others
						.stream()
						.allMatch(memberEnd -> association.getMemberEnd(memberEnd.getLeft(), memberEnd.getRight()) != null));
	}
	
	public static boolean hasAssociationClass(Class cls, String associationClassName, 
			List<MutablePair<String, Type>> others, 
			List<Property> associationOwnedAttributes) {
		return cls.getAssociations()
				.stream()
				.anyMatch(association -> 
					Associations.isAssociationClass(association)
					&&
					association.getName().equals(associationClassName) 
					&& others
						.stream()
						.allMatch(memberEnd -> association.getMemberEnd(memberEnd.getLeft(), memberEnd.getRight()) != null)
					&& associationOwnedAttributes
						.stream()
						.allMatch(attribute -> ((AssociationClass) association).getOwnedAttribute(attribute.getName(), attribute.getType()) != null));
	}
	
	public static boolean isLeafInGeneralizationStructure(Class cls) {
		return !getAllSuperClasses(cls).isEmpty() && getAllSubclasses(cls).isEmpty();
	}
}

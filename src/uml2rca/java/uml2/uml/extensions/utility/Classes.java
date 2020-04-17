package uml2rca.java.uml2.uml.extensions.utility;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

public class Classes {

	public static EList<Class> getAllSuperClasses(Class cls) {
		EList<Class> superClasses = new BasicEList<>();
		
		for (Class superClass: cls.getSuperClasses()) {
			superClasses.add(superClass);
			superClasses.addAll(getAllSuperClasses(superClass));
		}
		
		return superClasses;
	}

	public static EList<Class> getAllSubclasses(Class cls) {
		EList<Class> subClasses = new BasicEList<>();
		
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
		
		return subClasses;
	}
	
	public static boolean hasAttribute(Class cls, String name, Type type) {
		return cls.getOwnedAttributes()
		.stream()
		.anyMatch(attribute -> 
			attribute.getName().equals(name) 
				&& attribute.getType().getName().equals(type.getName()));
	}
	
	public static boolean isLeafInGeneralizationStructure(Class cls) {
		return !getAllSuperClasses(cls).isEmpty() && getAllSubclasses(cls).isEmpty();
	}
}

package rca.utility;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Type;

public class Classes {

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
		for (Package nestedPackage: Models.getAllNestedPackages(model)) {
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

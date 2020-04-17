package uml2rca.java.uml2.uml.extensions.utility;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;

public class Models {

	public static EList<Package> getAllNestedPackages(Model model) {
		EList<Package> allNestedPackages = new BasicEList<>();
		
		for (Package nestedPackage: model.getNestedPackages()) {
			allNestedPackages.add(nestedPackage);
			allNestedPackages.addAll(Packages.getAllNestedPackages(nestedPackage));
		}
		
		return allNestedPackages;
	}

}

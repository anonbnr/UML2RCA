package uml2rca.utility;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Package;

public class Packages {

	public static EList<Package> getAllNestedPackages(Package pckg) {
		EList<Package> allNestedPackages = new BasicEList<>();
		
		for (Package nestedPackage: pckg.getNestedPackages()) {
			allNestedPackages.add(nestedPackage);
			allNestedPackages.addAll(getAllNestedPackages(nestedPackage));
		}
		
		return allNestedPackages;
	}

}

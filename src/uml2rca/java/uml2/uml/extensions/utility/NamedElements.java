package uml2rca.java.uml2.uml.extensions.utility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class NamedElements {
	
	public static List<Dependency> getSupplierDependencies(NamedElement supplier) {
		List<Dependency> supplierDependencies = new ArrayList<>();
		supplier.getTargetDirectedRelationships()
		.stream()
		.filter(relation -> relation instanceof Dependency)
		.forEach(supplierDependency -> 
			supplierDependencies.add((Dependency)supplierDependency));
		
		return supplierDependencies;
	}
}

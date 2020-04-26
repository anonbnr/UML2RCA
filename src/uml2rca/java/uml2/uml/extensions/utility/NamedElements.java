package uml2rca.java.uml2.uml.extensions.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class NamedElements {
	
	public static List<Dependency> getDependencies(NamedElement namedElement) {
		return Stream
				.concat(namedElement.getClientDependencies().stream(), 
						NamedElements.getSupplierDependencies(namedElement).stream())
				.collect(Collectors.toList());
	}
	
	public static List<Dependency> getSupplierDependencies(NamedElement supplier) {
		return supplier.getTargetDirectedRelationships()
				.stream()
				.filter(relation -> relation instanceof Dependency)
				.map(relation -> (Dependency) relation)
				.collect(Collectors.toList());
	}
	
	public static boolean hasClientDependency(NamedElement namedElement, String dependencyName) {
		return namedElement.getClientDependencies()
				.stream()
				.anyMatch(clientDependency -> clientDependency.getName().equals(dependencyName));
	}
	
	public static List<Dependency> getClientDependencies(NamedElement namedElement, 
			String dependencyName) {
		
		return namedElement.getClientDependencies()
				.stream()
				.filter(clientDependency -> clientDependency.getName().equals(dependencyName))
				.collect(Collectors.toList());
	}
	
	public static boolean hasClientDependency(NamedElement namedElement, String dependencyName,
			List<NamedElement> suppliers) {
		
		return getClientDependencies(namedElement, dependencyName)
				.stream()
				.anyMatch(clientDependency -> 
					suppliers
					.stream()
					.allMatch(supplier -> 
						Dependencies.isDependencySupplier(clientDependency, supplier)));
	}
	
	public static List<Dependency> getClientDependencies(NamedElement namedElement, String dependencyName,
			List<NamedElement> suppliers) {
		
		return getClientDependencies(namedElement, dependencyName)
				.stream()
				.filter(clientDependency -> 
					suppliers
					.stream()
					.allMatch(supplier -> 
						Dependencies.isDependencySupplier(clientDependency, supplier)))
				.collect(Collectors.toList());
	}
	
	public static boolean hasSupplierDependency(NamedElement namedElement, String dependencyName) {
		return NamedElements.getSupplierDependencies(namedElement)
				.stream()
				.anyMatch(supplierDependency -> supplierDependency.getName().equals(dependencyName));
	}
	
	public static List<Dependency> getSupplierDependencies(NamedElement namedElement, 
			String dependencyName) {
		
		return NamedElements.getSupplierDependencies(namedElement)
				.stream()
				.filter(supplierDependency -> supplierDependency.getName().equals(dependencyName))
				.collect(Collectors.toList());
	}
	
	public static boolean hasSupplierDependency(NamedElement namedElement, String dependencyName,
			List<NamedElement> clients) {
		
		return getSupplierDependencies(namedElement, dependencyName)
		.stream()
		.anyMatch(supplierDependency -> 
			clients
			.stream()
			.allMatch(client -> 
				Dependencies.isDependencyClient(supplierDependency, client)));
	}
	
	public static List<Dependency> getSupplierDependencies(NamedElement namedElement, String dependencyName,
			List<NamedElement> clients) {
		
		return getSupplierDependencies(namedElement, dependencyName)
		.stream()
		.filter(supplierDependency -> 
			clients
			.stream()
			.allMatch(client -> 
				Dependencies.isDependencyClient(supplierDependency, client)))
		.collect(Collectors.toList());
	}
	
	public static boolean hasDependency(NamedElement namedElement, String dependencyName) {
		return hasClientDependency(namedElement, dependencyName)
				|| hasSupplierDependency(namedElement, dependencyName);
	}
	
	public static List<Dependency> getDependencies(NamedElement namedElement, String dependencyName) {
		return Stream
				.concat(getClientDependencies(namedElement, dependencyName).stream(),
						getSupplierDependencies(namedElement, dependencyName).stream())
				.collect(Collectors.toList());
	}
	
	public static boolean hasDependency(NamedElement namedElement, String dependencyName,
			List<NamedElement> others) {
		
		return getDependencies(namedElement, dependencyName)
				.stream()
				.anyMatch(dependency -> 
					others
					.stream()
					.allMatch(other -> hasDependency(other, dependencyName)));
	}
	
	public static List<Dependency> getDependencies(NamedElement namedElement, String dependencyName,
			 List<NamedElement> others) {
		
		return getDependencies(namedElement, dependencyName)
				.stream()
				.filter(dependency -> 
					others
					.stream()
					.allMatch(other -> hasDependency(other, dependencyName)))
				.collect(Collectors.toList());
	}
}

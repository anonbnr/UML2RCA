package uml2rca.java.uml2.uml.extensions.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class Dependencies {

	public static boolean isDependencyClient(Dependency dependency, NamedElement namedElement) {
		return dependency.getClients().contains(namedElement);
	}
	
	public static boolean isDependencySupplier(Dependency dependency, NamedElement namedElement) {
		return dependency.getSuppliers().contains(namedElement);
	}
	
	public static List<NamedElement> getOtherNamedElementsInDependency(Dependency dependency, NamedElement namedElement) {
		
		return Stream
				.concat(dependency.getClients().stream(), dependency.getSuppliers().stream())
				.filter(dependencyElement -> dependencyElement != namedElement)
				.collect(Collectors.toList());
	}
}

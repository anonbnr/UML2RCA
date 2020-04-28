package uml2rca.java.uml2.uml.extensions.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

public class Dependencies {

	public static boolean isDependencyClient(Dependency dependency, NamedElement namedElement) {
		return dependency.getClients().contains(namedElement);
	}
	
	public static boolean isDependencySupplier(Dependency dependency, NamedElement namedElement) {
		return dependency.getSuppliers().contains(namedElement);
	}
	
	public static List<NamedElement> getMembers(Dependency dependency) {
		return Stream
				.concat(dependency.getClients().stream(), 
						dependency.getSuppliers().stream())
				.collect(Collectors.toList());
	}
	
	public static List<NamedElement> getOtherNamedElementsInDependency(Dependency dependency, NamedElement namedElement) {
		return Stream
				.concat(dependency.getClients().stream(), dependency.getSuppliers().stream())
				.filter(dependencyElement -> dependencyElement != namedElement)
				.collect(Collectors.toList());
	}
	
	public static boolean hasAnAbstractMember(Dependency dependency) {
		return Stream
				.concat(dependency.getClients().stream(), 
						dependency.getSuppliers().stream())
				.map(namedElement -> (Class) namedElement)
				.anyMatch(cls -> cls.isAbstract());
	}
	
	public static Class getFirstAbstractMember(Dependency dependency) {
		return Stream
				.concat(dependency.getClients().stream(), 
						dependency.getSuppliers().stream())
				.map(namedElement -> (Class) namedElement)
				.filter(cls -> cls.isAbstract())
				.findFirst()
				.get();
	}
	
	public static NamedElement getFirstMemberHavingType(Dependency dependency, Class memberType) {
		return Stream
				.concat(dependency.getClients().stream(), 
						dependency.getSuppliers().stream())
				.map(namedElement -> (Class) namedElement)
				.filter(cls -> cls == memberType)
				.findFirst()
				.get();
	}
}

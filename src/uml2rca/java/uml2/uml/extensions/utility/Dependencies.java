package uml2rca.java.uml2.uml.extensions.utility;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Type;

import uml2rca.exceptions.NotATypeException;

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
	
	/**
	 * Checks whether the provided list of member ends of the dependency are typed elements
	 * @param source the dependency to examine
	 * @param ends the list of member ends to check 
	 * @throws NotATypeException if the list of member ends has a member end that is not a typed element
	 */
	public static void validateDependencyEnds(Dependency source, List<NamedElement> ends) 
			throws NotATypeException {
		
		if (!(ends
		.stream()
		.allMatch(end -> (end instanceof Type))))
			throw new NotATypeException(source.getName() + 
					" is a dependency having a non-type client/supplier");
	}
}

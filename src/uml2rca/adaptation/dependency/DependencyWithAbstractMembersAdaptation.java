package uml2rca.adaptation.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.adaptation.AbstractRelationshipWithAbstractMembersAdaptation;
import uml2rca.exceptions.NotADependencyWithAnAbstractMemberException;
import uml2rca.exceptions.NotATypeException;
import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.java.uml2.uml.extensions.utility.Dependencies;

/**
 * a DependencyWithAbstractMembersAdaptation concrete class that is used to adapt a UML
 * dependency having abstract members by moving down the dependency into each non abstract child 
 * of each of its abstract members.<br><br>
 * 
 * The adaptation consists of creating a list of target dependencies having the 
 * same owning package as the source dependency to adapt, such that the members of each 
 * target dependency consist of the original non abstract members of the source dependency 
 * and a non abstract child of the original abstract member of the source dependency.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Dependency
 */
public class DependencyWithAbstractMembersAdaptation extends AbstractRelationshipWithAbstractMembersAdaptation<Dependency> {
	/* CONSTRUCTORS */
	/**
	 * Creates an empty dependency with abstract members adaptation
	 */
	public DependencyWithAbstractMembersAdaptation() {}
	
	/**
	 * Creates a dependency with abstract members adaptation having source as its source
	 * dependency to adapt, then applies the adaptation to obtain the list of target
	 * dependencies with no abstract members, such that the members of each 
	 * target dependency consist of the original non abstract members of the source dependency
	 * and a non abstract child of the original abstract member of the source dependency.
	 * It then cleans the post-adaptation residues.
	 * @param source the source dependency to adapt
	 * @throws NotATypeException if the provided source dependency has a client or a supplier that is not a typed element 
	 * @throws NotAnAssociationWithAnAbstractMemberException if the provided source dependency 
	 * doesn't have abstract members
	 */
	public DependencyWithAbstractMembersAdaptation(Dependency source) 
			throws NotATypeException, NotADependencyWithAnAbstractMemberException{
		
		Dependencies.validateDependencyEnds(source, source.getClients());
		Dependencies.validateDependencyEnds(source, source.getSuppliers());
		
		if(!Dependencies.hasAnAbstractMember(source))
			throw new NotADependencyWithAnAbstractMemberException(source.getName() 
					+ " doesn't have any abstract members");
		
		apply(source);
	}

	/* METHODS */
	/**
	 * Creates a new empty list of dependencies 
	 */
	@Override
	protected List<Dependency> createNewRelationshipEmptyList() {
		return new ArrayList<>();
	}

	/**
	 * Initializes the the content of the abstract members dictionary
	 * of the source dependency with abstract members to adapt, by
	 * mapping each of its abstract members to the set of its non abstract
	 * subclasses.
	 */
	@Override
	protected void initSourceRelationshipAbstractMembersDictionary() {
		Stream
		.concat(source.getClients().stream(), 
				source.getSuppliers().stream())
		.map(namedElement -> (Class) namedElement)
		.filter(cls -> cls.isAbstract())
		.forEach(cls -> {
			abstractMembersDictionary.put(cls,
				Classes.getAllSubclasses(cls)
				.stream()
				.filter(subClass -> !subClass.isAbstract())
				.collect(Collectors.toSet()));
		});
	}

	/**
	 * Checks whether the provided dependency has an abstract member
	 */
	@Override
	protected boolean hasAnAbstractMember(Dependency dependency) {
		return Dependencies.hasAnAbstractMember(dependency);
	}

	/**
	 * Returns the first abstract member of the provided dependency
	 */
	@Override
	protected Class getFirstAbstractMember(Dependency dependency) {
		return Dependencies.getFirstAbstractMember(dependency);
	}
	
	/**
	 * Clones the provided enqueued intermediary dependency.
	 */
	@Override
	protected Dependency cloneIntermediaryRelationship(Dependency intermediaryDependency) {
		Dependency newOwnedDependency = UMLFactory.eINSTANCE.createDependency();
		
		intermediaryDependency.getClients()
		.stream()
		.forEach(client -> newOwnedDependency.getClients().add(client));
		
		intermediaryDependency.getSuppliers()
		.stream()
		.forEach(supplier -> newOwnedDependency.getSuppliers().add(supplier));
		
		return newOwnedDependency;
	}
	
	/**
	 * Adapts the first abstract member of the provided intermediary dependency 
	 * by replacing it with one of its non abstract subclasses.
	 */
	@Override
	protected Dependency adaptIntermediaryRelationship(Dependency intermediaryDependency, Class abstractMember, Class subClass) {
		
		if (Dependencies.isDependencyClient(intermediaryDependency, abstractMember)) {
			intermediaryDependency.getClients().remove(abstractMember);
			intermediaryDependency.getClients().add(subClass);
		}
		else {
			intermediaryDependency.getSuppliers().remove(abstractMember);
			intermediaryDependency.getSuppliers().add(subClass);
		}
		
		return intermediaryDependency;
	}

	/**
	 * Sets the name and the owning package for each target dependency 
	 * in the provided list of target dependencies.
	 * The owning package is the same one owning the source dependency
	 * with abstract members to adapt, whereas the name conforms to 
	 * the following convention &lt;sourceDependencyName&gt;-&lt;newNonAbstractMemberName&gt;
	 */
	@Override
	protected void initTargetRelationshipsNamesAndOwnership(List<Dependency> newOwnedDependencies) {
		List<NamedElement> sourceMembers = Dependencies.getMembers(source);
		
		newOwnedDependencies
		.stream()
		.forEach(newDependency -> {
			source.getNearestPackage().getPackagedElements().add(newDependency);
			String newDependencyName = source.getName();
			
			for (NamedElement member: Dependencies.getMembers(newDependency))
				if (!sourceMembers.contains(member))
					newDependencyName += "-" + member.getName();
			
			newDependency.setName(newDependencyName);
		});
	}
}
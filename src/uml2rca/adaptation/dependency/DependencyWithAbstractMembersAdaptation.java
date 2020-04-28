package uml2rca.adaptation.dependency;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotADependencyWithAnAbstractMemberException;
import uml2rca.exceptions.NotATypeException;
import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.java.uml2.uml.extensions.utility.Dependencies;

public class DependencyWithAbstractMembersAdaptation extends AbstractAdaptation<Dependency, List<Dependency>> {
	
	/* ATTRIBUTES */
	protected Map<Class, Set<Class>> abstractMembersDictionary;
	protected Queue<Dependency> newDependenciesQueue;
	protected List<Dependency> dependenciesToClean;
	
	/* CONSTRUCTOR */
	public DependencyWithAbstractMembersAdaptation(Dependency source) 
			throws NotADependencyWithAnAbstractMemberException, 
			NotATypeException {
		
		validateDependencyEnds(source, source.getClients());
		validateDependencyEnds(source, source.getSuppliers());
		
		if(!Dependencies.hasAnAbstractMember(source))
			throw new NotADependencyWithAnAbstractMemberException(source.getName() 
					+ " doesn't have any abstract members");
		
		this.setSource(source);
		abstractMembersDictionary = new Hashtable<>();
		newDependenciesQueue = new LinkedList<>();
		dependenciesToClean = new ArrayList<>();
		this.setTarget(transform(source));
		this.postTransformationClean();
	}

	/* METHODS */
	private void validateDependencyEnds(Dependency source, List<NamedElement> ends) throws NotATypeException {
		if (!(ends
		.stream()
		.allMatch(end -> (end instanceof Type))))
			throw new NotATypeException(source.getName() + 
					" is a dependency having a non-type client/supplier");
	}
	
	@Override
	public List<Dependency> transform(Dependency source) {
		List<Dependency> newOwnedDependencies = new ArrayList<>();
		initTargetDependencies(newOwnedDependencies);
		
		return newOwnedDependencies;
	}	
	
	protected List<Dependency> initTargetDependencies(List<Dependency> newOwnedDependencies) {
		initAbstractMemberEndsDictionary();
		
		newDependenciesQueue.add(source);
		
		while(!newDependenciesQueue.isEmpty()) {
			final Dependency currentDependency = newDependenciesQueue.element();
			
			if (Dependencies.hasAnAbstractMember(currentDependency)) {
				Class abstractMember = Dependencies.getFirstAbstractMember(
						currentDependency);
				
				abstractMembersDictionary.get(abstractMember)
				.stream()
				.forEach(subClass -> {
					Dependency clonedDependency = cloneSourceDependencyAndAdaptAbstractMember(
							currentDependency, abstractMember, subClass);
					newDependenciesQueue.add(clonedDependency);
				});
				
				dependenciesToClean.add(currentDependency);
			}
			
			else
				newOwnedDependencies.add(currentDependency);
			
			newDependenciesQueue.remove();
		}
		
		initTargetDependenciesNamesAndOwnership(newOwnedDependencies);
		
		return newOwnedDependencies;
	}
	
	protected void initAbstractMemberEndsDictionary() {
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
	
	protected Dependency cloneSourceDependencyAndAdaptAbstractMember(
			Dependency currentDependency, Class abstractMember,
			Class subClass) {
		
		Dependency newOwnedDependency = initTargetDependency(currentDependency);
		
		if (Dependencies.isDependencyClient(newOwnedDependency, abstractMember)) {
			newOwnedDependency.getClients().remove(abstractMember);
			newOwnedDependency.getClients().add(subClass);
		}
		else {
			newOwnedDependency.getSuppliers().remove(abstractMember);
			newOwnedDependency.getSuppliers().add(subClass);
		}
		
		return newOwnedDependency;
	}

	protected Dependency initTargetDependency(Dependency currentDependency) {
		Dependency newOwnedDependency = UMLFactory.eINSTANCE.createDependency();
		
		currentDependency.getClients()
		.stream()
		.forEach(client -> newOwnedDependency.getClients().add(client));
		
		currentDependency.getSuppliers()
		.stream()
		.forEach(supplier -> newOwnedDependency.getSuppliers().add(supplier));
		
		return newOwnedDependency;
	}
	
	protected void initTargetDependenciesNamesAndOwnership(List<Dependency> newOwnedDependencies) {
		
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

	protected void postTransformationClean() {
		dependenciesToClean
		.stream()
		.forEach(Dependency::destroy);
	}
}

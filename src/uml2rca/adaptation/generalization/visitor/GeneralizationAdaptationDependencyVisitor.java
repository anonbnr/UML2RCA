package uml2rca.adaptation.generalization.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.adaptation.generalization.dependency.conflict.GeneralizationAdaptationClassDependencyConflictCandidate;
import uml2rca.adaptation.generalization.dependency.conflict.GeneralizationAdaptationClassDependencyConflictScope;
import uml2rca.adaptation.generalization.dependency.conflict.OwningClassDependencyConflictSource;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.DefaultRenameDependencyConflictResolutionStrategy;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.DependencyConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.DiscardConflictingDependencyConflictResolutionStrategy;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.ExpertRenameDependencyConflictResolutionStrategy;
import uml2rca.exceptions.ConflictResolutionStrategyException;
import uml2rca.java.uml2.uml.extensions.utility.Dependencies;
import uml2rca.java.uml2.uml.extensions.utility.NamedElements;
import uml2rca.java.uml2.uml.extensions.visitor.IVisitableUMLElement;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableDependency;

public class GeneralizationAdaptationDependencyVisitor extends GeneralizationAdaptationClassAbstractVisitor<Dependency> {
	
	/* CONSTRUCTOR */
	public GeneralizationAdaptationDependencyVisitor(GeneralizationAdaptationClassVisitor sourceClassVisitor,
			Collection<Class> scope, DependencyConflictResolutionStrategyType conflictStrategyType) {
		
		super(sourceClassVisitor, conflictStrategyType);
		this.conflictScope = new GeneralizationAdaptationClassDependencyConflictScope(this, scope,
				new OwningClassDependencyConflictSource(sourceClassVisitor.getOwner()));
		
		this.conflictCandidate = new GeneralizationAdaptationClassDependencyConflictCandidate(this, conflictScope, 
				sourceClassVisitor.getOwner());
	}

	/* METHODS */
	public String getPostAdaptationIndirectlyOwnedDependencyDefaultName() {
		return visitedElement.getName() + "-" + sourceClassVisitor.getTarget().getName();
	}
	
	@Override
	public void visit(IVisitableUMLElement element) { // visits an owner's dependency
		VisitableDependency visitableDependency = (VisitableDependency) element;
		visitedElement = visitableDependency.getContainedDependency();
		
		try {
			initTargetClassDependency();
		} catch (ConflictResolutionStrategyException e) {
			e.printStackTrace();
		}
	}

	protected void initTargetClassDependency() throws ConflictResolutionStrategyException {
		
		if (conflictCandidate.satisfiesConflictCondition()) {
			conflictScope.setConflictSource(new OwningClassDependencyConflictSource(sourceClassVisitor.getOwner()));
			initDependencyConflictResolutionStrategy();
		}
		
		else
			initTargetClassNonConflictingDependency();
		
		if (toClean(visitedElement))
			toClean.add(visitedElement);
	}

	protected void initTargetClassNonConflictingDependency() {
		Dependency newDependency = UMLFactory.eINSTANCE.createDependency();
		visitedElement.getNearestPackage().getPackagedElements().add(newDependency);
		
		if (sourceClassVisitor.getOwner() != sourceClassVisitor.getSource())
			newDependency.setName(
					getPostAdaptationIndirectlyOwnedDependencyDefaultName());
		else
			newDependency.setName(visitedElement.getName());
		
		if (Dependencies.isDependencyClient(visitedElement, sourceClassVisitor.getOwner()))
			initTargetClassClientDependency(newDependency);
		else
			initTargetClassSupplierDependency(newDependency);
	}

	protected void initTargetClassClientDependency(Dependency newDependency) {
		
		visitedElement.getClients()
			.stream()
			.forEach(client -> {
				if (client == sourceClassVisitor.getOwner())
					newDependency.getClients().add(sourceClassVisitor.getTarget());
				else
					newDependency.getClients().add(client);
			});
		
		visitedElement.getSuppliers()
			.stream()
			.forEach(supplier -> newDependency.getSuppliers().add(supplier));
	}
	
	protected void initTargetClassSupplierDependency(Dependency newDependency) {
		
		visitedElement.getClients()
			.stream()
			.forEach(client -> newDependency.getClients().add(client));
		
		visitedElement.getSuppliers()
			.stream()
			.forEach(supplier -> {
				if (supplier == sourceClassVisitor.getOwner())
					newDependency.getSuppliers().add(sourceClassVisitor.getTarget());
				else
					newDependency.getSuppliers().add(supplier);
			});
	}
	
	protected void initDependencyConflictResolutionStrategy() throws ConflictResolutionStrategyException {
		initConflictSourcePreTransformationConflictingDependencies();
		
		if (conflictStrategyType == DependencyConflictResolutionStrategyType.DEFAULT_RENAME) {
			initConflictSourcePostTransformationConflictingAssociations();
			conflictStrategy = new DefaultRenameDependencyConflictResolutionStrategy(sourceClassVisitor.getTarget(), conflictScope);
		}
		
		else if (conflictStrategyType == DependencyConflictResolutionStrategyType.EXPERT_RENAME) {
			initConflictSourcePostTransformationConflictingAssociations();
			List<String> expertProvidedNames = Arrays.asList(
					"expertNameProvidedForOriginallyOwnedDependency",
					"expertNameProvidedForConflictingDependency");
			
			conflictStrategy = new ExpertRenameDependencyConflictResolutionStrategy(sourceClassVisitor.getTarget(),
					conflictScope, expertProvidedNames);
		}
		
		else if (conflictStrategyType == DependencyConflictResolutionStrategyType.DISCARD)
			conflictStrategy = new DiscardConflictingDependencyConflictResolutionStrategy(
					sourceClassVisitor.getTarget(), conflictScope);
	}

	protected void initConflictSourcePreTransformationConflictingDependencies() {
		List<NamedElement> others = Dependencies.getOtherNamedElementsInDependency(
				visitedElement, sourceClassVisitor.getOwner());
		
		conflictScope.getConflictSource().addPreTransformationConflictingElement(
				getPreAdaptationIndirectlyOwnedDependency(others));
		
		conflictScope.getConflictSource().addPreTransformationConflictingElement(visitedElement);
	}

	protected Dependency getPreAdaptationIndirectlyOwnedDependency(List<NamedElement> others) {
		return conflictScope.getScope()
			.stream()
			.filter(scopeClass ->
				scopeClass != conflictScope.getConflictSource().getSource() 
				&& NamedElements.hasDependency(scopeClass, visitedElement.getName(), others))
			.map(scopeClass -> NamedElements.getDependencies(scopeClass, visitedElement.getName(), others))
			.flatMap(list -> list.stream())
			.collect(Collectors.toList())
			.get(0);
	}
	
	protected void initConflictSourcePostTransformationConflictingAssociations() throws ConflictResolutionStrategyException {
		initTargetClassNonConflictingDependency();
		
		Stream
		.concat(sourceClassVisitor.getTarget().getClientDependencies().stream(), 
				NamedElements.getSupplierDependencies(sourceClassVisitor.getTarget()).stream())
		.filter(ownedDependency -> 
			ownedDependency.getName().equals(getPostAdaptationIndirectlyOwnedDependencyDefaultName()))
		.forEach(conflictingDependency -> 
			conflictScope.getConflictSource().addPostTransformationConflictingElement(conflictingDependency));
	}
	
	@Override
	protected boolean toClean(Dependency dependency) {
		return !toClean.contains(dependency)
				&& (sourceClassVisitor.getOwner() == sourceClassVisitor.getSource() 
					|| sourceClassVisitor.getVisitableSource().getSubClasses()
						.contains(sourceClassVisitor.getOwner()));
	}
}
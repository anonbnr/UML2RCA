package uml2rca.adaptation.generalization;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Property;

import core.adaptation.AbstractAdaptation;
import uml2rca.adaptation.generalization.association.conflict.resolution_strategy.AssociationConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.AttributeConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy.DependencyConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAssociationClassVisitor;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAssociationVisitor;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAttributeVisitor;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationClassVisitor;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationDependencyVisitor;
import uml2rca.exceptions.ConflictResolutionStrategyException;
import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.java.uml2.uml.extensions.utility.NamedElements;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableAssociation;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableAttribute;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableClass;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableDependency;

public class SimpleGeneralizationAdaptation extends AbstractAdaptation<Class, Class> {
	
	/* ATTRIBUTES */
	protected VisitableClass visitableSource;
	protected GeneralizationAdaptationClassVisitor classVisitor;
	protected List<GeneralizationAdaptationAttributeVisitor> attributeVisitors;
	protected List<GeneralizationAdaptationAssociationVisitor> associationVisitors;
	protected List<GeneralizationAdaptationDependencyVisitor> dependencyVisitors;
	protected List<Class> conflictScope;
	
	/* CONSTRUCTORS */
	public SimpleGeneralizationAdaptation() {}
	
	public SimpleGeneralizationAdaptation(Class leaf, Class choice) 
			throws NotALeafInGeneralizationHierarchyException, 
			NotAValidLevelForGeneralizationAdaptationException {
		
		if (!Classes.isLeafInGeneralizationStructure(leaf))
			throw new NotALeafInGeneralizationHierarchyException(leaf.getName() 
					+ " is not a leaf in a generalization hierarchy");
		
		if (!(leaf == choice || Classes.getAllSuperClasses(leaf).contains(choice)))
			throw new NotAValidLevelForGeneralizationAdaptationException(choice.getName()
					+ " is neither " + leaf.getName() + " nor one of its superclasses");
		
		apply(choice);
	}

	/* METHODS */
	public VisitableClass getVisitableSource() {
		return visitableSource;
	}
	
	public List<GeneralizationAdaptationAttributeVisitor> getAttributeVisitors() {
		return attributeVisitors;
	}

	public List<GeneralizationAdaptationAssociationVisitor> getAssociationVisitors() {
		return associationVisitors;
	}
	
	public List<GeneralizationAdaptationDependencyVisitor> getDependencyVisitors() {
		return dependencyVisitors;
	}
	
	@Override
	public void preTransform(Class choice) {
		super.preTransform(choice);
		visitableSource = new VisitableClass(choice);
		attributeVisitors = new ArrayList<>();
		associationVisitors = new ArrayList<>();
		dependencyVisitors = new ArrayList<>();
		conflictScope = visitableSource.getSubClasses();
	}
	
	@Override
	public Class transform(Class source) {
		
		initTargetClass();
		
		try {
			// add owned, inherited, and specializing attributes
			initTargetClassAttributes();
			
			// add owned, inherited, and specializing associations
			initTargetClassAssociations();
			
			// add owned, inherited, and specializing dependencies
			initTargetClassDependencies();
			
		} catch (ConflictResolutionStrategyException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		return classVisitor.getTarget();
	}

	protected Class initTargetClass() {
		classVisitor = new GeneralizationAdaptationClassVisitor(
						source, source, null);
		
		visitableSource.accept(classVisitor);
		return classVisitor.getTarget();
	}
	
	protected void initTargetClassAttribute(Property ownedAttribute, 
			AttributeConflictResolutionStrategyType attributeConflictResolutionStrategyType) {
		
		VisitableAttribute ownedVisitableAttribute = new VisitableAttribute(ownedAttribute);
		GeneralizationAdaptationAttributeVisitor attributeVisitor =
				new GeneralizationAdaptationAttributeVisitor(classVisitor, conflictScope, attributeConflictResolutionStrategyType);
		
		ownedVisitableAttribute.accept(attributeVisitor);
		attributeVisitors.add(attributeVisitor);
	}

	protected void initTargetClassAttributes()
			throws ConflictResolutionStrategyException {
		/* owned attributes */
		initTargetClassSourceAttributes(AttributeConflictResolutionStrategyType.NONE);
		
		/* inherited attributes (superclasses) */
		initTargetClassInheritedAttributes(AttributeConflictResolutionStrategyType.NONE);
		
		/* specializing attributes (subclasses) */
		initTargetClassSpecializingAttributes(AttributeConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}

	protected void initTargetClassSourceAttributes(AttributeConflictResolutionStrategyType
			attributeConflictResolutionStrategyType) {
		
		for (Property ownedAttribute: source.getAttributes())
			initTargetClassAttribute(ownedAttribute, 
					attributeConflictResolutionStrategyType);
	}
	
	protected void initTargetClassInheritedAttributes(AttributeConflictResolutionStrategyType
			attributeConflictResolutionStrategyType) {
		
		for (Class superClass: visitableSource.getSuperClasses()) {
			classVisitor.setOwner(superClass);
			
			for (Property ownedAttribute: superClass.getAttributes())
				initTargetClassAttribute(ownedAttribute,
						attributeConflictResolutionStrategyType);
		}
	}
	
	protected void initTargetClassSpecializingAttributes(AttributeConflictResolutionStrategyType
			attributeConflictResolutionStrategyType) {
		
		for (Class subClass: visitableSource.getSubClasses()) {
			classVisitor.setOwner(subClass);
			
			for (Property ownedAttribute: subClass.getAttributes())
				initTargetClassAttribute(ownedAttribute,
						attributeConflictResolutionStrategyType);
		}
	}
	
	protected void initTargetClassAssociation(Association ownedAssociation,
			AssociationConflictResolutionStrategyType associationConflictResolutionStrategyType) {
		
		VisitableAssociation ownedVisitableAssociation = new VisitableAssociation(ownedAssociation);
		GeneralizationAdaptationAssociationVisitor associationVisitor = null;
		
		if (Associations.isAssociationClass(ownedAssociation))
			associationVisitor = new GeneralizationAdaptationAssociationClassVisitor(classVisitor, conflictScope, 
					associationConflictResolutionStrategyType);
		
		else
			associationVisitor = new GeneralizationAdaptationAssociationVisitor(classVisitor, conflictScope, 
					associationConflictResolutionStrategyType);
		
		ownedVisitableAssociation.accept(associationVisitor);
		associationVisitors.add(associationVisitor);
	}
	
	protected void initTargetClassAssociations() {
		/* owned associations */
		initTargetClassSourceAssociations(AssociationConflictResolutionStrategyType.NONE);
		
		/* inherited associations (superclasses) */
		initTargetClassInheritedAssociations(AssociationConflictResolutionStrategyType.NONE);
		
		/* specializing associations (subclasses) */
		initTargetClassSpecializingAssociations(AssociationConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}

	protected void initTargetClassSourceAssociations(AssociationConflictResolutionStrategyType
			associationConflictResolutionStrategyType) {
		
		for (Association ownedAssociation: source.getAssociations())
			initTargetClassAssociation(ownedAssociation, 
					associationConflictResolutionStrategyType);
	}
	
	protected void initTargetClassInheritedAssociations(AssociationConflictResolutionStrategyType
			associationConflictResolutionStrategyType) {
		for (Class superClass: visitableSource.getSuperClasses()) {
			
			classVisitor.setOwner(superClass);
			
			for (Association ownedAssociation: superClass.getAssociations())
				initTargetClassAssociation(ownedAssociation,
						associationConflictResolutionStrategyType);
		}
	}
	
	protected void initTargetClassSpecializingAssociations(AssociationConflictResolutionStrategyType
			associationConflictResolutionStrategyType) {
		
		for (Class subClass: visitableSource.getSubClasses()) {
			classVisitor.setOwner(subClass);
			
			for (Association ownedAssociation: subClass.getAssociations())
				initTargetClassAssociation(ownedAssociation,
						associationConflictResolutionStrategyType);
		}
	}
	
	protected void initTargetClassDependency(Dependency ownedDependency,
			DependencyConflictResolutionStrategyType dependencyConflictResolutionStrategyType) {
		
		VisitableDependency ownedVisitableDependency = new VisitableDependency(ownedDependency);
		GeneralizationAdaptationDependencyVisitor dependencyVisitor =
				new GeneralizationAdaptationDependencyVisitor(classVisitor, conflictScope, dependencyConflictResolutionStrategyType);
		
		ownedVisitableDependency.accept(dependencyVisitor);
		dependencyVisitors.add(dependencyVisitor);
	}
	
	protected void initTargetClassDependencies() {
		/* owned dependencies */
		initTargetClassSourceDependencies(DependencyConflictResolutionStrategyType.NONE);
		
		/* inherited dependencies (superclasses) */
		initTargetClassInheritedDependencies(DependencyConflictResolutionStrategyType.NONE);
		
		/* specializing dependencies (subclasses) */
		initTargetClassSpecializingDependencies(DependencyConflictResolutionStrategyType.DEFAULT_RENAME);
		
		classVisitor.setOwner(source);
	}

	protected void initTargetClassSpecializingDependencies(
			DependencyConflictResolutionStrategyType dependencyConflictResolutionStrategyType) {
		
		for (Class subClass: visitableSource.getSubClasses()) {
			classVisitor.setOwner(subClass);
			
			for (Dependency ownedDependency: NamedElements.getDependencies(subClass))
				initTargetClassDependency(ownedDependency,
						dependencyConflictResolutionStrategyType);
		}
	}

	protected void initTargetClassInheritedDependencies(
			DependencyConflictResolutionStrategyType dependencyConflictResolutionStrategyType) {
		
		for (Class superClass: visitableSource.getSuperClasses()) {
			classVisitor.setOwner(superClass);
			
			for (Dependency ownedDependency: NamedElements.getDependencies(superClass))
				initTargetClassDependency(ownedDependency,
						dependencyConflictResolutionStrategyType);
		}
	}

	protected void initTargetClassSourceDependencies(
			DependencyConflictResolutionStrategyType dependencyConflictResolutionStrategyType) {
		
		for (Dependency ownedDependency: NamedElements.getDependencies(source))
			initTargetClassDependency(ownedDependency, 
					dependencyConflictResolutionStrategyType);
	}

	@Override
	public void postTransform(Class source) {
		// remove source's owned associations & dependencies, and its subclasses' associations & dependencies
		Stream
		.concat(associationVisitors.stream(), dependencyVisitors.stream())
		.map(visitor -> visitor.getToClean())
		.forEach(toClean -> 
			toClean
			.stream()
			.forEach(toCleanElement -> toCleanElement.destroy()));
		
		// remove source and its collapsed subclasses
		visitableSource.getSubClasses()
		.stream()
		.forEach(Class::destroy);
		
		source.destroy();
	}
}

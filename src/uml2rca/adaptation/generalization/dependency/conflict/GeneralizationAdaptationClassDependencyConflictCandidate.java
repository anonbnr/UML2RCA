package uml2rca.adaptation.generalization.dependency.conflict;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.NamedElement;

import core.conflict.AbstractConflictCandidate;
import core.conflict.AbstractConflictScope;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationDependencyVisitor;
import uml2rca.java.uml2.uml.extensions.utility.Dependencies;
import uml2rca.java.uml2.uml.extensions.utility.NamedElements;

public class GeneralizationAdaptationClassDependencyConflictCandidate extends AbstractConflictCandidate<Class, Dependency> {

	/* ATTRIBUTES */
	protected GeneralizationAdaptationDependencyVisitor dependencyVisitor;
	
	/* CONSTRUCTORS */
	public GeneralizationAdaptationClassDependencyConflictCandidate(
			GeneralizationAdaptationDependencyVisitor dependencyVisitor,
			AbstractConflictScope<Class, Dependency> conflictScope,
			Class candidate) {
		
		super(conflictScope, candidate);
		this.dependencyVisitor = dependencyVisitor;
	}
	
	/* METHODS */
	@Override
	public boolean satisfiesConflictCondition() {
		List<NamedElement> others = Dependencies.getOtherNamedElementsInDependency(
				dependencyVisitor.getVisitedElement(), dependencyVisitor.getSourceClassVisitor().getOwner());
		
		return NamedElements.hasDependency(dependencyVisitor.getSourceClassVisitor().getTarget(), 
				dependencyVisitor.getPostAdaptationIndirectlyOwnedDependencyDefaultName(), 
				others);
	}
}

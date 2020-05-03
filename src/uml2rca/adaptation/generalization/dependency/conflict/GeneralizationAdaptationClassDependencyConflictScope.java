package uml2rca.adaptation.generalization.dependency.conflict;

import java.util.Collection;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictScope;
import core.conflict.IConflictDomain;

public class GeneralizationAdaptationClassDependencyConflictScope extends AbstractConflictScope<Class, Dependency> {

	public GeneralizationAdaptationClassDependencyConflictScope(IConflictDomain<Class, Dependency> conflictDomain,
			Collection<Class> scope) {
		
		super(conflictDomain, scope);
	}
}

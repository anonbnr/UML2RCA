package uml2rca.adaptation.generalization.dependency.conflict;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictScope;
import core.conflict.AbstractConflictSource;

public class OwningClassDependencyConflictSource extends AbstractConflictSource<Class, Dependency> {

	public OwningClassDependencyConflictSource(AbstractConflictScope<Class, Dependency> conflictScope, Class entity) {
		super(conflictScope, entity);
	}
}

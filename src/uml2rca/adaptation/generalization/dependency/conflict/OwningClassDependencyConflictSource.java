package uml2rca.adaptation.generalization.dependency.conflict;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;

import core.conflict.AbstractConflictSource;

public class OwningClassDependencyConflictSource extends AbstractConflictSource<Class, Dependency> {

	public OwningClassDependencyConflictSource(Class source) {
		super(source);
	}
}

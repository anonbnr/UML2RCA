package uml2rca.adaptation.generalization.association.conflict;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictSource;

public class OwningClassAssociationConflictSource extends AbstractConflictSource<Class, Association> {

	public OwningClassAssociationConflictSource(Class source) {
		super(source);
	}
}

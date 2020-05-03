package uml2rca.adaptation.generalization.attribute.conflict;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictScope;
import core.conflict.AbstractConflictSource;

public class OwningClassAttributeConflictSource extends AbstractConflictSource<Class, Property> {

	public OwningClassAttributeConflictSource(AbstractConflictScope<Class, Property> conflictScope, Class entity) {
		super(conflictScope, entity);
	}
}

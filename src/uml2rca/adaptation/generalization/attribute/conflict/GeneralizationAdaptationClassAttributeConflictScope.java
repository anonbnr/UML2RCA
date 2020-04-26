package uml2rca.adaptation.generalization.attribute.conflict;

import java.util.Collection;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.conflict.AbstractConflictScope;
import core.conflict.AbstractConflictSource;
import core.conflict.IConflictDomain;

public class GeneralizationAdaptationClassAttributeConflictScope extends AbstractConflictScope<Class, Property> {

	public GeneralizationAdaptationClassAttributeConflictScope(IConflictDomain<Class, Property> conflictDomain, 
			Collection<Class> scope,
			AbstractConflictSource<Class, Property> conflictSource) {
		
		super(conflictDomain, scope, conflictSource);
	}
}

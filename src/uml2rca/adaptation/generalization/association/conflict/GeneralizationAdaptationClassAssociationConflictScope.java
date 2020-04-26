package uml2rca.adaptation.generalization.association.conflict;

import java.util.Collection;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictScope;
import core.conflict.AbstractConflictSource;
import core.conflict.IConflictDomain;

public class GeneralizationAdaptationClassAssociationConflictScope extends AbstractConflictScope<Class, Association> {

	public GeneralizationAdaptationClassAssociationConflictScope(IConflictDomain<Class, Association> conflictDomain, Collection<Class> scope,
			AbstractConflictSource<Class, Association> conflictSource) {
		
		super(conflictDomain, scope, conflictSource);
	}
}

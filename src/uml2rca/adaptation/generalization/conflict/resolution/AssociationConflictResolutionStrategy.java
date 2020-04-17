package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

public abstract class AssociationConflictResolutionStrategy extends ConflictResolutionStrategy<Association> {

	public AssociationConflictResolutionStrategy(Class target, List<Association> conflicting, 
			boolean sameSemantics) {
		super(target, conflicting, sameSemantics);
	}

}

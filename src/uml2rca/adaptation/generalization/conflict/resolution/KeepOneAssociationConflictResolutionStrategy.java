package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;

public class KeepOneAssociationConflictResolutionStrategy extends AssociationConflictResolutionStrategy {

	public KeepOneAssociationConflictResolutionStrategy(Class target, List<Association> conflicting) {
		super(target, conflicting, true);
		resolve();
	}

	@Override
	protected void resolve() {
		// do nothing and keep the association already owned by the target class
	}

}

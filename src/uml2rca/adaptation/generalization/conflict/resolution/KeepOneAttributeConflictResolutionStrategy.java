package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

public class KeepOneAttributeConflictResolutionStrategy extends AttributeConflictResolutionStrategy {

	public KeepOneAttributeConflictResolutionStrategy(Class target, List<Property> conflicting) {
		super(target, conflicting, true);
		resolve();
	}

	@Override
	protected void resolve() {
		// do nothing and keep the attribute already owned by the target class
	}

}

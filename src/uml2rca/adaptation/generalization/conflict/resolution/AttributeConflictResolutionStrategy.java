package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

public abstract class AttributeConflictResolutionStrategy extends ConflictResolutionStrategy<Property> {

	public AttributeConflictResolutionStrategy(Class target, List<Property> conflicting, 
			boolean sameSemantics) {
		super(target, conflicting, sameSemantics);
	}

}

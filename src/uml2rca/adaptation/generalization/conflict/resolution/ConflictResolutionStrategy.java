package uml2rca.adaptation.generalization.conflict.resolution;

import java.util.List;

import org.eclipse.uml2.uml.Class; 

public abstract class ConflictResolutionStrategy<T> {
	/* ATTRIBUTES */
	protected Class target;
	protected List<T> conflicting;
	protected boolean sameSemantics;
	
	/* CONSTRUCTORS */
	public ConflictResolutionStrategy(Class target, List<T> conflicting, 
			boolean sameSemantics) {
		this.target = target;
		this.conflicting = conflicting;
		this.sameSemantics = sameSemantics;
	}
	
	/* METHODS */
	protected abstract void resolve();
}

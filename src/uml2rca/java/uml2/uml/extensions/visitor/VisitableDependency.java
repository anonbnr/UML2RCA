package uml2rca.java.uml2.uml.extensions.visitor;

import org.eclipse.uml2.uml.Dependency;

public class VisitableDependency implements IVisitableUMLElement {
	
	/* ATTRIBUTES */
	protected Dependency containedDependency;

	/* CONSTRUCTOR */
	public VisitableDependency(Dependency containedDependency) {
		this.containedDependency = containedDependency;
	}

	/* METHODS */
	public Dependency getContainedDependency() {
		return containedDependency;
	}

	public void setContainedDependency(Dependency containedDependency) {
		this.containedDependency = containedDependency;
	}

	@Override
	public void accept(IUMLElementVisitor visitor) {
		visitor.visit(this);
	}

}

package uml2rca.java.uml2.uml.extensions.visitor;

import org.eclipse.uml2.uml.Association;

public class VisitableAssociation implements IVisitableUMLElement {
	
	/* ATTRIBUTES */
	protected Association containedAssociation;

	/* CONSTRUCTOR */
	public VisitableAssociation(Association containedAssociation) {
		this.containedAssociation = containedAssociation;
	}

	/* METHODS */
	public Association getContainedAssociation() {
		return containedAssociation;
	}

	public void setContainedAssociation(Association containedAssociation) {
		this.containedAssociation = containedAssociation;
	}

	@Override
	public void accept(IUMLElementVisitor visitor) {
		visitor.visit(this);
	}
}

package uml2rca.java.uml2.uml.extensions.visitor;

import org.eclipse.uml2.uml.Property;

public class VisitableAttribute implements IVisitableUMLElement {
	
	/* ATTRIBUTES */
	protected Property containedAttribute;

	/* CONSTRUCTOR */
	public VisitableAttribute(Property containedAttribute) {
		this.containedAttribute = containedAttribute;
	}

	/* METHODS */
	public Property getContainedAttribute() {
		return containedAttribute;
	}

	public void setContainedAttribute(Property containedAttribute) {
		this.containedAttribute = containedAttribute;
	}

	@Override
	public void accept(IUMLElementVisitor visitor) {
		visitor.visit(this);
	}
}

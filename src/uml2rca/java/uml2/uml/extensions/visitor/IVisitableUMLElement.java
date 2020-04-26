package uml2rca.java.uml2.uml.extensions.visitor;

public interface IVisitableUMLElement {
	void accept(IUMLElementVisitor visitor);
}

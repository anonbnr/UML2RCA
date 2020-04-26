package uml2rca.adaptation.generalization.visitor;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.java.uml2.uml.extensions.visitor.IUMLElementVisitor;
import uml2rca.java.uml2.uml.extensions.visitor.VisitableClass;
import uml2rca.java.uml2.uml.extensions.visitor.IVisitableUMLElement;

public class GeneralizationAdaptationClassVisitor implements IUMLElementVisitor {
	
	/* ATTRIBUTES */
	protected Class source;
	protected VisitableClass visitableSource;
	protected Class owner;
	protected Class target;
	
	/* CONSTRUCTORS */
	public GeneralizationAdaptationClassVisitor() {
		
	}
	
	public GeneralizationAdaptationClassVisitor(Class source, Class owner, Class target) {
		this.source = source;
		this.visitableSource = new VisitableClass(source);
		this.owner = owner;
		this.target = target;
	}

	/* METHODS */
	public Class getSource() {
		return source;
	}

	public void setSource(Class source) {
		this.source = source;
	}

	public VisitableClass getVisitableSource() {
		return visitableSource;
	}

	public void setVisitableSource(VisitableClass visitableSource) {
		this.visitableSource = visitableSource;
	}

	public Class getOwner() {
		return owner;
	}

	public void setOwner(Class owner) {
		this.owner = owner;
	}
	
	public Class getTarget() {
		return target;
	}

	public void setTarget(Class target) {
		this.target = target;
	}
	
	@Override
	public void visit(IVisitableUMLElement element) { // visits an owner
		VisitableClass visitableOwner = (VisitableClass) element;
		owner = visitableOwner.getContainedClass();
		this.initTargetClass();
	}
	
	protected void initTargetClass() {
		target = UMLFactory.eINSTANCE.createClass();
		
		// initialize the class' name and package
		target.setName(source.getName());
		target.setPackage(source.getPackage());
	}
}

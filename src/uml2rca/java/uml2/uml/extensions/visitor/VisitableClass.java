package uml2rca.java.uml2.uml.extensions.visitor;

import java.util.List;

import org.eclipse.uml2.uml.Class;

import uml2rca.java.uml2.uml.extensions.utility.Classes;

public class VisitableClass implements IVisitableUMLElement {
	/* ATTRIBUTES */
	protected Class containedClass;
	protected List<Class> superClasses;
	protected List<Class> subClasses;

	/* CONSTRUCTOR */
	public VisitableClass(Class containedClass) {
		setContainedClass(containedClass);
	}
	
	/* METHODS */
	public Class getContainedClass() {
		return containedClass;
	}

	public void setContainedClass(Class containedClass) {
		this.containedClass = containedClass;
		this.superClasses = Classes.getAllSuperClasses(containedClass);
		this.subClasses = Classes.getAllSubclasses(containedClass);
	}
	
	public List<Class> getSuperClasses() {
		return superClasses;
	}

	public List<Class> getSubClasses() {
		return subClasses;
	}

	@Override
	public void accept(IUMLElementVisitor visitor) {
		visitor.visit(this);
	}
}

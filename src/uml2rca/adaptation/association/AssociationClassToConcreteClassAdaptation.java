package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an AssociationClassToConcreteClassAdaptation concrete class that is used to adapt a UML
 * association class into an equivalent concrete class.<br/><br/>
 * 
 * The adaptation consists of:
 * <ol>
 * <li>creating an equivalent target concrete class, having the same owning package, 
 * the same name, and all owned attributes of the source association class.</li>
 * <li>creating n binary associations between the created class and the n types participating
 * in the source association class.</li>
 * </ol>
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see AssociationClass
 * @see Class
 */
public class AssociationClassToConcreteClassAdaptation extends AbstractAdaptation<AssociationClass, Class> {
	
	/* ATTRIBUTES */
	List<Association> associations;
	
	/* CONSTRUCTOR */
	public AssociationClassToConcreteClassAdaptation(AssociationClass source) {
		this.setSource(source);
		associations = new ArrayList<>();
		this.setTarget(this.transform(source));
		postTransformationClean();
	}

	/* METHODS */
	public List<Association> getAssociations() {
		return associations;
	}
	
	// implementation of the IAdaptation interface
	@Override
	public Class transform(AssociationClass source) {
		Class target = initMaterializedClass(source);
		initMaterializedClassAttributes(source, target);
		initMaterializedClassAssociations(source, target); 
		
		return target;
	}
	
	private Class initMaterializedClass(AssociationClass source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		cls.setPackage(source.getPackage());
		cls.setName(Strings.capitalize(source.getName()));
		
		return cls;
	}

	private void initMaterializedClassAttributes(AssociationClass source, Class target) {
		source.getOwnedAttributes()
		.stream()
		.forEach(ownedAttribute -> 
			target.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType()));
	}

	private void initMaterializedClassAssociations(AssociationClass source, Class cls) {
		Property firstEnd = source.getMemberEnds().get(0);
		Property secondEnd = source.getMemberEnds().get(1);
		
		initMaterializedClassAssociation(source, cls, firstEnd, secondEnd);
		initMaterializedClassAssociation(source, cls, secondEnd, firstEnd);
	}

	private void initMaterializedClassAssociation(AssociationClass source, 
			Class target, Property nonTargetEnd, Property targetEnd) {
		
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setPackage(source.getPackage());
		association.setName(nonTargetEnd.getType().getName() + "-" + target.getName());
		
		initMaterializedClassAssociationTargetEnd(association, target, targetEnd, nonTargetEnd);
		initMaterializedClassAssociationNonTargetEnd(association, nonTargetEnd);
		
		associations.add(association);
	}

	protected void initMaterializedClassAssociationTargetEnd(Association association, 
			Class target, Property targetEnd, Property nonTargetEnd) {
		
		Property newTargetEnd = Associations.cloneMemberEnd(targetEnd);
		Associations.adaptMemberEndOwnership(
				association, newTargetEnd, targetEnd.isNavigable());
		
		newTargetEnd.setType(target);
		newTargetEnd.setName(
				Strings.decapitalize(target.getName()) 
				+ "-" 
				+ nonTargetEnd.getType().getName()
		);
		
		newTargetEnd.setAggregation(AggregationKind.NONE_LITERAL);
	}

	protected void initMaterializedClassAssociationNonTargetEnd(
			Association association, Property nonTargetEnd) {
		
		Property newNonTargetEnd = Associations.cloneMemberEnd(nonTargetEnd);
		Associations.adaptMemberEndOwnership(
				association, newNonTargetEnd, nonTargetEnd.isNavigable());
		
		newNonTargetEnd.setLower(1);
		newNonTargetEnd.setUpper(1);
	}
	
	private void postTransformationClean() {
		source.destroy();
	}
}
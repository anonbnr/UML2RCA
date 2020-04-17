package uml2rca.adaptation.association;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
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
	
	/* CONSTRUCTOR */
	public AssociationClassToConcreteClassAdaptation(AssociationClass source) {
		super(source);
		postTransformationClean();
	}

	/* METHOD */
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

	private void initMaterializedClassAssociation(AssociationClass source, Class target, Property nonTargetEnd, Property targetEnd) {
		
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setName(nonTargetEnd.getType().getName() + "-" + target.getName());
		association.setPackage(source.getPackage());
		
		Property newNonTargetEnd = Associations.cloneMemberEnd(nonTargetEnd);
		Associations.adaptMemberEndOwnership(
				association, newNonTargetEnd, nonTargetEnd.isNavigable());
		
		newNonTargetEnd.setLower(1);
		newNonTargetEnd.setUpper(1);
		
		Property newTargetEnd = Associations.cloneMemberEnd(targetEnd);
		Associations.adaptMemberEndOwnership(
				association, newTargetEnd, targetEnd.isNavigable());
		
		newTargetEnd.setAggregation(AggregationKind.NONE_LITERAL);
		newTargetEnd.setName(
				Strings.decapitalize(target.getName()) 
				+ "-" 
				+ nonTargetEnd.getType().getName()
		);
		
		newTargetEnd.setType(target);
	}
	
	private EList<Property> getAssociatedClassifiersOwnedAttributesToClean(AssociationClass associationClass, Property navigableEnd) {
		
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: associationClass.getMemberEnds()) {
			Class endCls = (Class) end.getType();
			
			for (Property attribute: endCls.getAllAttributes()) {
				if (attribute.getName().equals(navigableEnd.getName())
						&& attribute.getType().equals(navigableEnd.getType())
						&& attribute.getAssociation() == associationClass)
					toClean.add(attribute);
			}
		}
		
		return toClean;
	}
	
	private void cleanAssociatedClassifiersOwnedAttributes(AssociationClass associationClass) {
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: associationClass.getMemberEnds())
			if (end.isNavigable())
				toClean.addAll(getAssociatedClassifiersOwnedAttributesToClean(associationClass, end));
		
		for (Property attribute: toClean)
			attribute.destroy();
	}
	
	private void postTransformationClean() {
		cleanAssociatedClassifiersOwnedAttributes(source);
		source.destroy();
	}
}
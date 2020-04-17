package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnNAryAssociationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.utility.Associations;

/**
 * an NaryAssociationMaterializationAdaptation concrete class that is used to adapt 
 * a UML n-ary association by reifying it into an equivalent concrete class.<br/><br/>
 * 
 * The adaptation consists of:
 * <ol>
 * <li>creating an equivalent target concrete class, having the same owning package, 
 * and the same name of the source n-ary association.</li>
 * <li>creating n binary associations between the created class and the n types participating
 * in the source n-ary association.</li>
 * </ol> 
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class NaryAssociationMaterializationAdaptation extends NaryAssociationAdaptation<Class> {
	
	/* CONSTRUCTOR */
	public NaryAssociationMaterializationAdaptation(Association source)
			throws NotAnNAryAssociationException {
		super(source);
	}

	/* METHODS */	
	// implementation of the IAdaptation interface
	@Override
	public Class transform(Association source) {
		Class target = initMaterializedClass(source);
		initMaterializedClassAssociations(source, target); 
		
		return target;
	}
	
	private Class initMaterializedClass(Association source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		cls.setPackage(source.getPackage());
		cls.setName(Strings.capitalize(source.getName()));
		
		return cls;
	}
	
	private void initMaterializedClassAssociations(Association source, Class target) {
		
		EList<Property> memberEnds = source.getMemberEnds();
		
		for (Property end: memberEnds) {
			boolean targetEndNavigability = getMaterializedClassMemberEndNavigability(memberEnds, end);
			initMaterializedClassAssociation(source, target, end, targetEndNavigability);
		}
	}
	
	private boolean getMaterializedClassMemberEndNavigability(EList<Property> memberEnds, Property nonTargetEnd) {
		List<Property> otherEnds = new ArrayList<>();
		otherEnds.addAll(memberEnds);
		otherEnds.remove(nonTargetEnd);
		
		boolean targetEndNavigability = otherEnds
				.stream()
				.map(Property::isNavigable)
				.reduce(false, (nav1, nav2) -> nav1 || nav2);
		
		return targetEndNavigability;
	}
	
	private void initMaterializedClassAssociation(Association source, Class target, Property nonTargetEnd, boolean navigabilityOfTargetEnd) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setName(nonTargetEnd.getType().getName() + "-" + target.getName());
		association.setPackage(source.getPackage());
		
		Property newNonTargetEnd = Associations.cloneMemberEnd(nonTargetEnd);
		Associations.adaptMemberEndOwnership(
				association, newNonTargetEnd, nonTargetEnd.isNavigable());
		
		Property newTargetEnd = UMLFactory.eINSTANCE.createProperty();
		Associations.adaptMemberEndOwnership(association, newTargetEnd, navigabilityOfTargetEnd);
		
		newTargetEnd.setAggregation(AggregationKind.NONE_LITERAL);
		newTargetEnd.setName(
				Strings.decapitalize(target.getName()) 
				+ "-" 
				+ nonTargetEnd.getType().getName()
		);
		newTargetEnd.setLower(1);
		newTargetEnd.setUpper(1);
		newTargetEnd.setType(target);
	}
	
	private EList<Property> getAssociatedClassifiersOwnedAttributesToClean(Property navigableEnd) {
			
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: source.getMemberEnds()) {
			Class endCls = (Class) end.getType();
			
			for (Property attribute: endCls.getAllAttributes()) {
				if (attribute.getName().equals(navigableEnd.getName())
						&& attribute.getType().equals(navigableEnd.getType())
						&& attribute.getAssociation() == source)
					toClean.add(attribute);
			}
		}
		
		return toClean;
	}
	
	private void cleanAssociatedClassifiersOwnedAttributes() {
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: source.getMemberEnds())
			if (end.isNavigable())
				toClean.addAll(getAssociatedClassifiersOwnedAttributesToClean(end));
				
		for (Property attribute: toClean)
			attribute.destroy();
	}
	
	@Override
	protected void postTransformationClean() {
		cleanAssociatedClassifiersOwnedAttributes();
		source.destroy();
	}
}
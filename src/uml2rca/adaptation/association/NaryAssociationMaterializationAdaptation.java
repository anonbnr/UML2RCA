package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

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
public class NaryAssociationMaterializationAdaptation extends AbstractNaryAssociationAdaptation<Class> {
	
	/* CONSTRUCTOR */
	public NaryAssociationMaterializationAdaptation(Association source)
			throws NotAnNaryAssociationException {
		super(source);
	}
	
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
		
		List<Property> memberEnds = source.getMemberEnds();
		
		for (Property end: memberEnds) {
			boolean targetEndNavigability = getMaterializedClassMemberEndNavigability(memberEnds, end);
			initMaterializedClassAssociation(source, target, end, targetEndNavigability);
		}
	}
	
	public static boolean getMaterializedClassMemberEndNavigability(List<Property> memberEnds, 
			Property nonTargetEnd) {
		
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
		
		association.setPackage(source.getPackage());
		association.setName(nonTargetEnd.getType().getName() + "-" + target.getName());
		
		Property newNonTargetEnd = Associations.cloneMemberEnd(nonTargetEnd);
		Associations.adaptMemberEndOwnership(
				association, newNonTargetEnd, nonTargetEnd.isNavigable());
		
		Property newTargetEnd = UMLFactory.eINSTANCE.createProperty();
		Associations.adaptMemberEndOwnership(association, newTargetEnd, navigabilityOfTargetEnd);
		
		newTargetEnd.setType(target);
		newTargetEnd.setName(
				Strings.decapitalize(target.getName()) 
				+ "-" 
				+ nonTargetEnd.getType().getName()
		);
		newTargetEnd.setLower(1);
		newTargetEnd.setUpper(1);
		newTargetEnd.setAggregation(AggregationKind.NONE_LITERAL);
		
		associations.add(association);
	}
	
	@Override
	protected void postTransformationClean() {
		source.destroy();
	}
}
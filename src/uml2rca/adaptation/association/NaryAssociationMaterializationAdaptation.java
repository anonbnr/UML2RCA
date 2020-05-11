package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an NaryAssociationMaterializationAdaptation concrete class that is used to adapt 
 * a UML n-ary association by reifying it into an equivalent concrete class.<br><br>
 * 
 * The adaptation consists of:
 * <ol>
 * <li>creating an equivalent target concrete class, having the same owning package, 
 * and the same name of the source n-ary association.</li>
 * <li>creating n binary associations between the created target concrete class and the n types participating
 * in the source n-ary association.</li>
 * </ol> 
 * 
 * @author Bachar Rima
 * @see AbstractNaryAssociationAdaptation
 * @see Association
 */
public class NaryAssociationMaterializationAdaptation extends AbstractNaryAssociationAdaptation<Class> {
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty n-ary association materialization adaptation
	 */
	public NaryAssociationMaterializationAdaptation() {}
	
	/**
	 * Creates an n-ary association materialization adaptation having source as its source n-ary association 
	 * to adapt, then applies the adaptation to obtain the target class and the n binary associations, 
	 * and cleans the post-adaptation residues.
	 * @param source the source n-ary association to adapt
	 * @throws NotAnNaryAssociationException if the provided source entity is not an n-ary association
	 */
	public NaryAssociationMaterializationAdaptation(Association source) 
			throws NotAnNaryAssociationException {
		
		super(source);
	}
	
	/* METHODS */
	/**
	 * Creates an equivalent target concrete class, having the same owning package 
	 * and the same name as the source n-ary association to adapt, 
	 * then creates n binary associations between the target class and the n types 
	 * participating in source
	 */
	@Override
	public Class transform(Association source) {
		Class target = initTargetClass(source);
		initTargetClassAssociations(source, target); 
		
		return target;
	}
	
	/**
	 * Creates a target concrete class, having the same owning package and the same name as source
	 * @param source the source n-ary association to adapt
	 * @return the target concrete class, having the same owning package and the same name as source
	 */
	protected Class initTargetClass(Association source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		cls.setPackage(source.getPackage());
		cls.setName(Strings.capitalize(source.getName()));
		
		return cls;
	}
	
	/**
	 * Creates n binary associations between the target class and the n types participating in source
	 * @param source the source n-ary association to adapt
	 * @param target the target concrete class, having the same owning package and the same name as source
	 */
	protected void initTargetClassAssociations(Association source, Class target) {
		source.getMemberEnds()
		.stream()
		.forEach(end -> initTargetClassAssociation(source, target, end));
	}
	
	/**
	 * Creates a binary association between the target class and the provided non target type member end.
	 * @param source the source n-ary association to adapt
	 * @param target the target concrete class, having the same owning package and the same name as source
	 * @param nonTargetEnd the original non target type member end participating 
	 * in the source n-ary association to adapt
	 */
	protected void initTargetClassAssociation(Association source, Class target, Property nonTargetEnd) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setPackage(source.getPackage());
		association.setName(nonTargetEnd.getType().getName() + "-" + target.getName());
		
		initNonTargetClassMemberEnd(association, nonTargetEnd);
		initTargetClassMemberEnd(association, target, nonTargetEnd);
		
		associations.add(association);
	}

	/**
	 * Creates the non target type member end for the generated binary association
	 * @param association the generated binary association
	 * @param nonTargetEnd the original non target type member end participating 
	 * in the source n-ary association to adapt
	 */
	protected void initNonTargetClassMemberEnd(Association association, Property nonTargetEnd) {
		Property newNonTargetEnd = Associations.cloneMemberEnd(nonTargetEnd);
		Associations.adaptMemberEndOwnership(
				association, newNonTargetEnd, nonTargetEnd.isNavigable());
		
		newNonTargetEnd.setLower(1);
		newNonTargetEnd.setUpper(1);
	}

	/**
	 * Creates the target member end for the generated binary association
	 * @param association the generated binary association
	 * @param target the target concrete class, having the same owning package and the same name as source
	 * @param nonTargetEnd the original non target type member end participating 
	 * in the source n-ary association to adapt 
	 */
	protected void initTargetClassMemberEnd(Association association, Class target, Property nonTargetEnd) {
		Property newTargetEnd = UMLFactory.eINSTANCE.createProperty();
		Associations.adaptMemberEndOwnership(association, newTargetEnd, 
				getTargetClassMemberEndNavigability(source.getMemberEnds(), nonTargetEnd));
		
		newTargetEnd.setType(target);
		newTargetEnd.setName(
				Strings.decapitalize(target.getName()) 
				+ "-" 
				+ nonTargetEnd.getType().getName()
		);
		newTargetEnd.setLower(nonTargetEnd.getLower());
		newTargetEnd.setUpper(nonTargetEnd.getUpper());
		newTargetEnd.setAggregation(AggregationKind.NONE_LITERAL);
	}
	
	/**
	 * Computes the navigability of the target member end in the generated binary association
	 * relating the target concrete class to the non target type (i.e. one of the types 
	 * participating in the original source n-ary association to adapt), where the non target 
	 * type is expressed through the provided non target member end.<br><br>
	 *  
	 * The target member end is navigable if at least one of the other non target
	 * member ends (i.e. other than the one provided for this method) 
	 * in the source n-ary association to adapt are navigable.
	 * @param memberEnds the list of the original member ends of the source n-ary association to adapt
	 * @param nonTargetEnd the provided member end of the non target type (participating in the 
	 * source n-ary association to adapt), which will be related to the target concrete class 
	 * in the generated binary association
	 * @return true if the target member end is navigable in the generated binary association, 
	 * false otherwise
	 */
	public static boolean getTargetClassMemberEndNavigability(List<Property> memberEnds, 
			Property nonTargetEnd) {
		
		List<Property> otherEnds = new ArrayList<>();
		otherEnds.addAll(memberEnds);
		otherEnds.remove(nonTargetEnd);
		
		return otherEnds
				.stream()
				.map(Property::isNavigable)
				.reduce(false, (nav1, nav2) -> nav1 || nav2);
	}
	
	/**
	 * Removes the source n-ary association from the model once its equivalent target class
	 * and n target binary associations have been created and added to the model to replace it.
	 */
	@Override
	public void postTransform(Association source) {
		source.destroy();
	}
}
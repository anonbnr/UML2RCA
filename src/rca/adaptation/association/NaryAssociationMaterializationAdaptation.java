package rca.adaptation.association;

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
import exceptions.NotAnNAryAssociationException;
import utility.Strings;

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
		
		Class cls = initMaterializedClass(source);
		initMaterializedClassEndAssociation(source, cls); 
		cleanAssociatedClassifiersOwnedAttributes(source);
		
		source.destroy();
		
		return cls;
	}
	
	private Class initMaterializedClass(Association source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		cls.setPackage(source.getPackage());
		cls.setName(Strings.capitalize(source.getName()));
		
		return cls;
	}
	
	private void initMaterializedClassEndAssociation(Association source, Class cls) {
		
		EList<Property> memberEnds = source.getMemberEnds();
		List<Property> otherEnds = null;
		
		for (Property end: memberEnds) {
			otherEnds = new ArrayList<>();
			otherEnds.addAll(memberEnds);
			otherEnds.remove(end);
			
			boolean navigabilityOfClsEnd = otherEnds
					.stream()
					.map(Property::isNavigable)
					.reduce(false, (nav1, nav2) -> nav1 || nav2);
			
			cls.createAssociation(
					end.isNavigable(), 
					end.getAggregation(), 
					end.getName(),
					end.getLower(), 
					end.getUpper(), 
					end.getType(),
					navigabilityOfClsEnd, 
					AggregationKind.NONE_LITERAL, 
					Strings.decapitalize(cls.getName()) + "-" + end.getType().getName(), 
					1, 
					1
			);
		}
	}
	
	private EList<Property> getAssociatedClassifiersOwnedAttributesToClean(Association association, Property navigableEnd) {
			
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: association.getMemberEnds()) {
			Class endCls = (Class) end.getType();
			
			for (Property attribute: endCls.getAllAttributes()) {
				if (attribute.getName().equals(navigableEnd.getName())
						&& attribute.getType().equals(navigableEnd.getType())
						&& attribute.getAssociation() == association)
					toClean.add(attribute);
			}
		}
		
		return toClean;
	}
	
	private void cleanAssociatedClassifiersOwnedAttributes(Association association) {
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: association.getMemberEnds())
			if (end.isNavigable())
				toClean.addAll(getAssociatedClassifiersOwnedAttributesToClean(association, end));
				
		for (Property attribute: toClean)
			attribute.destroy();
	}
}
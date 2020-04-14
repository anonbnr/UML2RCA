package rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import utility.Strings;

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
	public AssociationClassToConcreteClassAdaptation(AssociationClass source) {super(source);}

	/* METHOD */
	// implementation of the IAdaptation interface
	@Override
	public Class transform(AssociationClass source) {
		
		Class cls = initMaterializedClass(source);
		initMaterializedClassEndAssociation(source, cls); 
		cleanAssociatedClassifiersOwnedAttributes(source);
		
		source.destroy();
		
		return cls;
	}
	
	private Class initMaterializedClass(AssociationClass source) {
		Class cls = UMLFactory.eINSTANCE.createClass();
		cls.setPackage(source.getPackage());
		cls.setName(Strings.capitalize(source.getName()));
		
		source.getOwnedAttributes()
		.stream()
		.forEach(ownedAttribute -> 
			cls.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType()));
		
		return cls;
	}

	private void initMaterializedClassEndAssociation(AssociationClass source, Class cls) {

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
}
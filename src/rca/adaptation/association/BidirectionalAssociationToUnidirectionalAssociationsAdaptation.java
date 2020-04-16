package rca.adaptation.association;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import core.adaptation.AbstractAdaptation;
import exceptions.NotABidirectionalAssociationException;
import rca.utility.Associations;

/**
 * a BidirectionalAssociationToUnidirectionalAssociationsAdaptation concrete class that is used to adapt a UML
 * bidirectional binary association into a set of equivalent unidirectional binary association.<br/><br/>
 * 
 * The adaptation consists of creating a set of unidirectional binary associations for each direction
 * of the bidirectional association, having the same owning package and the same name as the source bidirectional association.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class BidirectionalAssociationToUnidirectionalAssociationsAdaptation extends AbstractAdaptation<Association, EList<Association>> {
	
	/* CONSTRUCTOR */
	/**
	 * creates an BidirectionalAssociationToUnidirectionalAssociationsAdaptation instance from a source
	 * bidirectional association.
	 * @param source a bidirectional association to adapt.
	 * @throws NotABidirectionalAssociationException
	 */
	public BidirectionalAssociationToUnidirectionalAssociationsAdaptation(Association source)
			throws NotABidirectionalAssociationException {
		
		if(!Associations.isBidirectional(source))
			throw new NotABidirectionalAssociationException(source.getName() + 
					" is not a bidirectional association");
		
		this.setSource(source);
		this.setTarget(this.transform(source));
		postTransformationClean();
	}

	/* METHODS */
	// implementation of the IAdaptation interface
	@Override
	public EList<Association> transform(Association source) {
		
		EList<Association> uniAssociations = new BasicEList<>();
		Property firstEnd = source.getMemberEnds().get(0);
		Property secondEnd = source.getMemberEnds().get(1);
		
		uniAssociations.add(initUnidirectionalAssociation(
				source, firstEnd, secondEnd, "first-" + source.getName()));
		
		uniAssociations.add(initUnidirectionalAssociation(
				source, secondEnd, firstEnd, "second-" + source.getName()));
		
		return uniAssociations;
	}

	private Association initUnidirectionalAssociation(Association source, Property navigableEnd, Property nonNavigableEnd, String newName) {
		Association uniAssociation = Associations.cloneIntoUnidirectionalAssociation(navigableEnd, nonNavigableEnd);
		uniAssociation.setPackage(source.getPackage());
		uniAssociation.setName(newName);
		
		return uniAssociation;
	}
	
	private EList<Property> getAssociatedClassifiersOwnedAttributesToClean(
			Association unidirectionalAssociation, Property navigableEnd) {
		
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: unidirectionalAssociation.getMemberEnds()) {
			Class endCls = (Class) end.getType();
			
			for (Property attribute: endCls.getAllAttributes()) {
				if (attribute.getName().equals(navigableEnd.getName())
						&& attribute.getType().equals(navigableEnd.getType())
						&& attribute.getAssociation() == null)
					toClean.add(attribute);
			}
		}
		
		return toClean;
	}

	private void cleanAssociatedClassifiersOwnedAttributes(Association unidirectionalAssociation) {
		EList<Property> toClean = new BasicEList<>();
		
		for (Property end: unidirectionalAssociation.getMemberEnds())
			if (end.isNavigable())
				toClean.addAll(getAssociatedClassifiersOwnedAttributesToClean(unidirectionalAssociation, end));
		
		for (Property attribute: toClean)
			attribute.destroy();
	}
	
	private void postTransformationClean() {
		target.stream().forEach(this::cleanAssociatedClassifiersOwnedAttributes);
		source.destroy();
	}
}

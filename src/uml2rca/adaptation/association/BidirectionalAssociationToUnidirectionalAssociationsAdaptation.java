package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotABidirectionalAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * a BidirectionalAssociationToUnidirectionalAssociationsAdaptation concrete class that is used to adapt a UML
 * bidirectional binary association into a couple of equivalent target unidirectional binary associations.<br><br>
 * 
 * The adaptation consists of creating a couple of target unidirectional binary associations, 
 * one for each direction of the source bidirectional association to adapt, having the same owning package as it.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class BidirectionalAssociationToUnidirectionalAssociationsAdaptation extends AbstractAdaptation<Association, List<Association>> {
	
	/* CONSTRUCTOR */
	/**
	 * Creates a bidirectional association to unidirectional associations adaptation having source as its source
	 * bidirectional association to adapt, then applies the adaptation to obtain 
	 * the target unidirectional binary associations, and cleans the post-adaptation residues.
	 * @param source the source bidirectional association to adapt
	 * @throws NotABidirectionalAssociationException if the provided source entity is not a bidirectional association
	 */
	public BidirectionalAssociationToUnidirectionalAssociationsAdaptation(Association source)
			throws NotABidirectionalAssociationException {
		
		if(!Associations.isBidirectional(source))
			throw new NotABidirectionalAssociationException(source.getName() + 
					" is not a bidirectional association");
		
		apply(source);
	}

	/* METHODS */
	/**
	 * Creates an equivalent couple of unidirectional binary associations, 
	 * having the same owning package as the source bidirectional association to adapt, 
	 * one for each direction.
	 */
	@Override
	public List<Association> transform(Association source) {
		return initTargetUnidirectionalAssociations(source);
	}

	/**
	 * Creates a couple of equivalent target unidirectional binary associations from source
	 * @param source the source bidirectional association to adapt
	 * @return the list containing the couple of equivalent target unidirectional binary associations
	 */
	protected List<Association> initTargetUnidirectionalAssociations(Association source) {
		List<Association> uniAssociations = new ArrayList<>();
		
		Property firstEnd = source.getMemberEnds().get(0);
		Property secondEnd = source.getMemberEnds().get(1);
		
		uniAssociations.add(initTargetUnidirectionalAssociation(
				source, firstEnd, secondEnd, "first-" + source.getName()));
		
		uniAssociations.add(initTargetUnidirectionalAssociation(
				source, secondEnd, firstEnd, "second-" + source.getName()));
		
		return uniAssociations;
	}

	/**
	 * Creates a unidirectional binary association from source, having navigableEnd as its navigable end,
	 * nonNavigableEnd as its non navigable end, and newName as its name
	 * @param source the source bidirectional association to adapt
	 * @param navigableEnd a member end in the source bidirectional association to adapt that'll become
	 * the navigable end of the unidirectional binary association returned by this method
	 * @param nonNavigableEnd a member end in the source bidirectional association to adapt that'll become
	 * the non navigable end of the unidirectional binary association returned by this method
	 * @param newName the new name of the unidirectional binary association returned by this method
	 * @return a unidirectional binary association from source, having navigableEnd as its navigable end,
	 * nonNavigableEnd as its non navigable end, and newName as its name
	 */
	private Association initTargetUnidirectionalAssociation(Association source, Property navigableEnd, 
			Property nonNavigableEnd, String newName) {
		
		Association uniAssociation = Associations.cloneIntoUnidirectionalAssociation(navigableEnd, 
				nonNavigableEnd);
		
		uniAssociation.setPackage(source.getPackage());
		uniAssociation.setName(newName);
		
		return uniAssociation;
	}
	
	/**
	 * Removes the source bidirectional association from the model once its equivalent couple
	 * of target unidirectional binary associations have been created and added to the model to replace it.
	 */
	@Override
	public void postTransform(Association source) {		
		source.destroy();
	}
}

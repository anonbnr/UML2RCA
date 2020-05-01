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
 * bidirectional binary association into a set of equivalent unidirectional binary association.<br/><br/>
 * 
 * The adaptation consists of creating a set of unidirectional binary associations for each direction
 * of the bidirectional association, having the same owning package and the same name as the source bidirectional association.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class BidirectionalAssociationToUnidirectionalAssociationsAdaptation extends AbstractAdaptation<Association, List<Association>> {
	
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
	public List<Association> transform(Association source) {
		
		List<Association> uniAssociations = initTargetUnidirectionalAssociations(source);
		
		return uniAssociations;
	}

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

	private Association initTargetUnidirectionalAssociation(Association source, Property navigableEnd, 
			Property nonNavigableEnd, String newName) {
		
		Association uniAssociation = Associations.cloneIntoUnidirectionalAssociation(navigableEnd, 
				nonNavigableEnd);
		
		uniAssociation.setPackage(source.getPackage());
		uniAssociation.setName(newName);
		
		return uniAssociation;
	}
	
	private void postTransformationClean() {		
		source.destroy();
	}
}

package rca.adaptation.association;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import core.adaptation.AbstractAdaptation;
import exceptions.NotAnNAryAssociationException;
import rca.utility.Associations;

/**
 * an NaryAssociationToBinaryAssociationsAdaptation concrete class that is used to adapt 
 * a UML n-ary association by replacing it with the complete graph of n binary associations
 * between the n types participating in it.<br/><br/>
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class NaryAssociationToBinaryAssociationsAdaptation extends NaryAssociationAdaptation<EList<Association>> {
	
	/* CONSTRUCTOR */
	public NaryAssociationToBinaryAssociationsAdaptation(Association source) 
			throws NotAnNAryAssociationException {
		super(source);
	}

	/* METHODS */
	// implementation of the IAdaptation interface
	@Override
	public EList<Association> transform(Association source) {
		
		EList<Property> memberEnds = source.getMemberEnds();
		EList<Association> binaryAssociations = new BasicEList<>();
		Property iEnd = null, jEnd = null;
		
		for (int i = 0; i < memberEnds.size(); i++) {
			iEnd = memberEnds.get(i);
			
			for (int j = i + 1; j < memberEnds.size(); j++) {
				jEnd = memberEnds.get(j);
				binaryAssociations.add(initBinaryAssociation(
						source, iEnd, jEnd, 
						iEnd.getName() + "-" + source.getName() + "-" + jEnd.getName()));
			}
		}
		
		return binaryAssociations;
	}

	private Association initBinaryAssociation(Association source, Property firstEnd, Property secondEnd, String newName) {
		Association binaryAssociation = Associations.cloneIntoBinaryAssociation(firstEnd, secondEnd);
		binaryAssociation.setPackage(source.getPackage());
		binaryAssociation.setName(newName);
		
		return binaryAssociation;
	}
	
	@Override
	protected void postTransformationClean() {
		source.destroy();
	}
}

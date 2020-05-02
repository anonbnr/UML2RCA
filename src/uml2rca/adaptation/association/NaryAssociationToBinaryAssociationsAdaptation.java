package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an NaryAssociationToBinaryAssociationsAdaptation concrete class that is used to adapt 
 * a UML n-ary association by replacing it with the complete graph of n binary associations
 * between the n types participating in it.<br/><br/>
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public class NaryAssociationToBinaryAssociationsAdaptation extends AbstractNaryAssociationAdaptation<List<Association>> {
	
	/* CONSTRUCTOR */
	public NaryAssociationToBinaryAssociationsAdaptation(Association source) 
			throws NotAnNaryAssociationException {
		super(source);
		associations = target;
	}

	/* METHODS */
	// implementation of the IAdaptation interface
	@Override
	public List<Association> transform(Association source) {
		
		List<Property> memberEnds = source.getMemberEnds();
		List<Association> binaryAssociations = new ArrayList<>();
		Property iEnd = null, jEnd = null;
		
		for (int i = 0; i < memberEnds.size(); i++) {
			iEnd = memberEnds.get(i);
			
			for (int j = i + 1; j < memberEnds.size(); j++) {
				jEnd = memberEnds.get(j);
				binaryAssociations.add(initTargetBinaryAssociation(
						source, iEnd, jEnd, 
						iEnd.getName() + "-" + source.getName() + "-" + jEnd.getName()));
			}
		}
		
		return binaryAssociations;
	}

	private Association initTargetBinaryAssociation(Association source, Property firstEnd, Property secondEnd, 
			String newName) {
		
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

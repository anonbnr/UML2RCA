package rca.adaptation.association;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import exceptions.NotABinaryAssociationException;
import rca.utility.Associations;

/**
 * an AggregationToAssociationAdaptation concrete class that is used to adapt a UML
 * aggregation into an equivalent general association.<br/><br/>
 * 
 * The adaptation consists of adapting the aggregation into an equivalent general association
 * having the same name as the source aggregation.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 * @see AggregationKind
 */
public class AggregationToAssociationAdaptation extends AbstractAdaptation<Association, Association>{
	
	/* CONSTRUCTOR */
	/**
	 * creates an AggregationToAssociationAdaptation instance from a source aggregation.
	 * @param source an aggregation to adapt.
	 * @throws NotABinaryAssociationException
	 */
	public AggregationToAssociationAdaptation(Association source) 
			throws NotABinaryAssociationException {
		
		if(!source.isBinary())
			throw new NotABinaryAssociationException(source.getName() + 
					" is not an aggregation");
		
		this.setSource(source);
		this.setTarget(this.transform(source));
	}
	
	/* METHOD */
	// implementation of the IAdaptation interface
	@Override
	public Association transform(Association source) {
		return Associations.toGeneralAssociation(source, AggregationKind.SHARED_LITERAL);
	}
}
package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnAggregationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an AggregationToAssociationAdaptation concrete class that is used to adapt a UML
 * aggregation into an equivalent general association.<br><br>
 * 
 * The adaptation consists of adapting the aggregation into an equivalent target general association
 * whose member ends have no aggregation kind.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 * @see AggregationKind
 */
public class AggregationToAssociationAdaptation extends AbstractAdaptation<Association, Association> {
	
	/* CONSTRUCTOR */
	/**
	 * creates an aggregation to general association adaptation having source as its 
	 * source aggregation to adapt, then applies the adaptation to obtain the target general
	 * association.
	 * @param source the source aggregation to adapt.
	 * @throws NotAnAggregationException if the provided source entity is not an aggregation
	 */
	public AggregationToAssociationAdaptation(Association source) 
			throws NotAnAggregationException {
		
		if(!Associations.isAggregation(source))
			throw new NotAnAggregationException(source.getName() + 
					" is not an aggregation");
		
		apply(source);
	}
	
	/* METHODS */
	/**
	 * Adapts the source aggregation into a general association by removing the shared aggregation kind
	 * of its aggregated member end.
	 */
	@Override
	public Association transform(Association source) {
		return Associations.toGeneralAssociation(source, AggregationKind.SHARED_LITERAL);
	}
	
	/**
	 * Does nothing, as there's nothing to be done after the adaptation
	 */
	@Override
	public void postTransform(Association source) {
		
	}
}
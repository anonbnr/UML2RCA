package rca.adaptation.association;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import exceptions.NotABinaryAssociationException;
import rca.utility.Associations;

/**
 * a CompositionToAssociationAdaptation concrete class that is used to adapt a UML
 * composition into an equivalent general association.<br/><br/>
 * 
 * The adaptation consists of adapting the composition into an equivalent general association
 * having the same name as the source composition.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 * @see AggregationKind
 */
public class CompositionToAssociationAdaptation extends AbstractAdaptation<Association, Association> {
	
	/* CONSTRUCTOR */
	/**
	 * creates an CompositionToAssociationAdaptation instance from a source composition.
	 * @param source a composition to adapt.
	 * @throws NotABinaryAssociationException
	 */
	public CompositionToAssociationAdaptation(Association source)
			throws NotABinaryAssociationException {
		
		if(!source.isBinary())
			throw new NotABinaryAssociationException(source.getName() + " is not a composition");
		
		this.setSource(source);
		this.setTarget(this.transform(source));
	}

	/* METHOD */
	// implementation of the IAdaptation interface
	@Override
	public Association transform(Association source) {
		return Associations.toGeneralAssociation(source, AggregationKind.COMPOSITE_LITERAL);
	}
}
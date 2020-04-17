package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotACompositionException;
import uml2rca.utility.Associations;

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
	 * @throws NotACompositionException
	 */
	public CompositionToAssociationAdaptation(Association source)
			throws NotACompositionException {
		
		if(!Associations.isComposition(source))
			throw new NotACompositionException(source.getName() + " is not a composition");
		
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
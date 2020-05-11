package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotACompositionException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * a CompositionToAssociationAdaptation concrete class that is used to adapt a UML
 * composition into an equivalent general association.<br><br>
 * 
 * The adaptation consists of adapting the composition into an equivalent target general association
 * whose member ends have no aggregation kind.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 * @see AggregationKind
 */
public class CompositionToAssociationAdaptation extends AbstractAdaptation<Association, Association> {
	
	/* CONSTRUCTOR */
	/**
	 * creates a composition to general association adaptation having source as its 
	 * source composition to adapt, then applies the adaptation to obtain the target general
	 * association.
	 * @param source the source composition to adapt.
	 * @throws NotACompositionException if the provided source entity is not a composition
	 */
	public CompositionToAssociationAdaptation(Association source)
			throws NotACompositionException {
		
		if(!Associations.isComposition(source))
			throw new NotACompositionException(source.getName() + " is not a composition");
		
		apply(source);
	}

	/* METHODS */
	/**
	 * Adapts the source composition into a general association by removing the composite aggregation kind
	 * of its aggregated member end.
	 */
	@Override
	public Association transform(Association source) {
		return Associations.toGeneralAssociation(source, AggregationKind.COMPOSITE_LITERAL);
	}
	
	/**
	 * Does nothing, as there's nothing to be done after the adaptation
	 */
	@Override
	public void postTransform(Association source) {
		
	}
}
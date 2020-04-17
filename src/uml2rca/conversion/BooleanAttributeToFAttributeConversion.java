package uml2rca.conversion;

import org.eclipse.uml2.uml.Property;

import core.conversion.AbstractConversion;
import rca.FAttribute;
import rca.RCAFactory;

/**
 * a BooleanAttributeToFAttributeConversion concrete class that is used to convert a R-UML-conforming boolean attribute
 * into an equivalent RCA formal attribute.<br/><br/>
 * 
 * The conversion consists of creating an equivalent target formal attribute
 * having the same name as the source boolean attribute.
 * 
 * @author Bachar Rima
 * @see AbstractConversion
 * @see Property
 * @see FAttribute
 */
public class BooleanAttributeToFAttributeConversion extends AbstractConversion<Property, FAttribute> {
	
	/* CONSTRUCTOR */
	public BooleanAttributeToFAttributeConversion(Property source) {super(source);}

	/* METHODS */
	// implementation of the IConversion interface
	@Override
	public FAttribute transform(Property source) {
		FAttribute attribute = RCAFactory.eINSTANCE.createFAttribute();
		attribute.setName(source.getName());
		
		return attribute;
	}
}
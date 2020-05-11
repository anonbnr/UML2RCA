package uml2rca.conversion;

import org.eclipse.uml2.uml.Class;

import core.conversion.AbstractConversion;
import rca.Attribute;
import rca.AttributeSet;
import rca.FContext;
import rca.RCAFactory;

/**
 * a ClassToFContextConversion concrete class that is used to convert a R-UML-conforming concrete class
 * into an equivalent RCA formal context.<br/><br/>
 * 
 * The conversion consists of creating an equivalent target formal context 
 * having the same name as the source concrete class.
 * 
 * @author Bachar Rima
 * @see AbstractConversion
 * @see Class
 * @see FContext
 */
public class ClassToFContextConversion extends AbstractConversion<Class, FContext>{
	
	/* CONSTRUCTOR */
	public ClassToFContextConversion(Class source) {super(source);}

	/* METHODS */
	// implementation of the IConversion interface
	@Override
	public FContext transform(Class source) {
		FContext context = RCAFactory.eINSTANCE.createFContext();
		
		context.setName(source.getName());
		context.setAttributeSet(convertSourceAttributes());
		
		return context;
	}
	
	private AttributeSet convertSourceAttributes() {
		AttributeSet attributeSet = RCAFactory.eINSTANCE.createAttributeSet();
		
		this.source.getOwnedAttributes()
		.stream()
		.forEach(property -> 
			attributeSet.getAttributes().add(
					(Attribute) (new BooleanAttributeToFAttributeConversion(property)).getTarget()
			)
		);
		
		return attributeSet;
	}
	
	@Override
	public void postTransform(Class source) {
		
	}
}

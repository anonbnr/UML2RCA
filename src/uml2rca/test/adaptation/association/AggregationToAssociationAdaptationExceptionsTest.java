package uml2rca.test.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.junit.Ignore;
import org.junit.Test;

import uml2rca.adaptation.association.AggregationToAssociationAdaptation;
import uml2rca.exceptions.NotAnAggregationException;

public class AggregationToAssociationAdaptationExceptionsTest extends AggregationToAssociationAdaptationTest {

	/* ATTRIBUTES */
	protected Association nonAggregation;
	
	/* METHODS */
	@Override
	public void initializeAssociations() {
		nonAggregation = (Association) model.getPackagedElement("nonAggregation");
	}
	
	@Ignore
	@Override
	public void testTransformation() {
		
	}
	
	@Test(expected=NotAnAggregationException.class)
	public void testNotAnAggregationException() throws NotAnAggregationException {
			transformation = new AggregationToAssociationAdaptation(nonAggregation);
	}
	
	@Override
	public void tearDown() {
		
	}
}

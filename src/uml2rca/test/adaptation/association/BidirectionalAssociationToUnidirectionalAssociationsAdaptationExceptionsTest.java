package uml2rca.test.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.junit.Ignore;
import org.junit.Test;

import uml2rca.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptation;
import uml2rca.exceptions.NotABidirectionalAssociationException;

public class BidirectionalAssociationToUnidirectionalAssociationsAdaptationExceptionsTest
		extends BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest {
	
	/* ATTRIBUTES */
	protected Association nonBidirectional;
	
	@Override
	public void initializeAssociations() {
		nonBidirectional = (Association) model.getPackagedElement("nonBidirectional");
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Ignore
	@Override
	public void testTransformation() {
		
	}
	
	@Test(expected=NotABidirectionalAssociationException.class)
	public void testNotABidirectionalAssociationException() throws NotABidirectionalAssociationException {
			transformation = new BidirectionalAssociationToUnidirectionalAssociationsAdaptation(
					nonBidirectional);
	}
}

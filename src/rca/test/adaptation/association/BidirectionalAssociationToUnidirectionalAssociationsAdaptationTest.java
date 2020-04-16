package rca.test.adaptation.association;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import exceptions.NotABidirectionalAssociationException;
import rca.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptation;
import rca.management.EcoreModelManager;
import rca.utility.Associations;

public class BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.load("model/test/adaptation/association/source/bidirectional.uml");
		
		Association bidirectional = (Association) model.getPackagedElement("bidirectional");
		BidirectionalAssociationToUnidirectionalAssociationsAdaptation adaptation = null;
		
		try {
			adaptation = new BidirectionalAssociationToUnidirectionalAssociationsAdaptation
					(bidirectional);
		} catch (NotABidirectionalAssociationException e) {
			e.printStackTrace();
		}
		
		manager.save(model, "model/test/adaptation/association/target/bidirectional.uml");
		Association first = (Association) model.getMember("first-bidirectional");
		Association second = (Association) model.getMember("second-bidirectional");
		
		assertEquals(adaptation.getTarget().size(), 2);
		assertNotNull(first);
		assertNotNull(second);
		assertTrue(adaptation.getTarget().contains(first));
		assertTrue(adaptation.getTarget().contains(second));
		assertTrue(Associations.isUnidirectional(first));
		assertTrue(Associations.isUnidirectional(second));
	}
}

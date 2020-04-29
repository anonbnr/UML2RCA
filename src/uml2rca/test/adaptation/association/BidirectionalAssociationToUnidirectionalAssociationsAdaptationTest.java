package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptation;
import uml2rca.exceptions.NotABidirectionalAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest {

	@Test
	public void testTransformation() {
		String sourceFileName = "bidirectional.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = null;
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		Model model = modelManager.getModel();
		
		String bidirectionalName = "bidirectional";
		Association bidirectional = (Association) model.getPackagedElement(bidirectionalName);
		BidirectionalAssociationToUnidirectionalAssociationsAdaptation adaptation = null;
		
		try {
			adaptation = new BidirectionalAssociationToUnidirectionalAssociationsAdaptation
					(bidirectional);
		} catch (NotABidirectionalAssociationException e) {
			e.printStackTrace();
		}
		
		String firstName = "first-bidirectional";
		String secondName = "second-bidirectional";
		Association first = (Association) model.getMember(firstName);
		Association second = (Association) model.getMember(secondName);
		
		assertEquals(adaptation.getTarget().size(), 2);
		assertNotNull(first);
		assertNotNull(second);
		assertTrue(adaptation.getTarget().contains(first));
		assertTrue(adaptation.getTarget().contains(second));
		assertTrue(Associations.isUnidirectional(first));
		assertTrue(Associations.isUnidirectional(second));
		
		try {
			modelManager.saveStateAndExport(targetURI, model, "Bidirectional Association Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
}

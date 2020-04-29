package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.NaryAssociationToBinaryAssociationsAdaptation;
import uml2rca.exceptions.NotAnNAryAssociationException;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class NaryAssociationToBinaryAssociationsAdaptationTest {
	
	@Test
	public void testTransformation() {
		String sourceFileName = "naryAssociation.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = "naryAssociationForgotten.uml";
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = null;
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		Model model = modelManager.getModel();
		Package root = (Package) model.getPackagedElement("root");
		
		String naryAssociationName = "naryAssociation";
		Association naryAssociation = (Association) root.getPackagedElement(naryAssociationName);
		int naryAssociationMemberEndsSize = naryAssociation.getMemberEnds().size();
		EList<Association> associations = null;
		
		try {
			associations = new NaryAssociationToBinaryAssociationsAdaptation(naryAssociation).getTarget();
		} catch (NotAnNAryAssociationException e) {
			e.printStackTrace();
		}
		
		Association associationAB = (Association) root.getPackagedElement(
				"a-" + naryAssociationName + "-b");
		if (associationAB == null)
			associationAB = (Association) root.getPackagedElement(
					"b-" + naryAssociationName + "-a");
		
		Association associationAC = (Association) root.getPackagedElement(
				"a-" + naryAssociationName + "-c");
		if (associationAC == null)
			associationAC = (Association) root.getPackagedElement(
					"c-" + naryAssociationName + "-a");
		
		Association associationBC = (Association) root.getPackagedElement(
				"b-" + naryAssociationName + "-c");
		if (associationBC == null)
			associationBC = (Association) root.getPackagedElement(
					"c-" + naryAssociationName + "-b");
		
		assertNotNull(associationAB);
		assertNotNull(associationAC);
		assertNotNull(associationBC);
		
		assertTrue(associations.contains(associationAB));
		assertTrue(associations.contains(associationAC));
		assertTrue(associations.contains(associationBC));
		assertEquals(associations.size(), naryAssociationMemberEndsSize);
		
		try {
			modelManager.saveStateAndExport(targetURI, model, 
					"N-ary Association Adaptation (Forgotten association solution)", 
					EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
}

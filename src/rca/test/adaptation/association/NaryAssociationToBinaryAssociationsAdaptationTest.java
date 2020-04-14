package rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import exceptions.NotAnNAryAssociationException;
import rca.adaptation.association.NaryAssociationToBinaryAssociationsAdaptation;
import rca.management.EcoreModelManager;

public class NaryAssociationToBinaryAssociationsAdaptationTest {
	
	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceFileName = "naryAssociation.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = "naryAssociationForgotten.uml";
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		Model model = (Model) manager.load(sourceURI);
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
		
		manager.save(model, targetURI);
		
		Association associationAB = (Association) root.getPackagedElement("a-" + naryAssociationName + "-b");
		if (associationAB == null)
			associationAB = (Association) root.getPackagedElement("b-" + naryAssociationName + "-a");
		
		Association associationAC = (Association) root.getPackagedElement("a-" + naryAssociationName + "-c");
		if (associationAC == null)
			associationAC = (Association) root.getPackagedElement("c-" + naryAssociationName + "-a");
		
		Association associationBC = (Association) root.getPackagedElement("b-" + naryAssociationName + "-c");
		if (associationBC == null)
			associationBC = (Association) root.getPackagedElement("c-" + naryAssociationName + "-b");
		
		assertNotNull(associationAB);
		assertNotNull(associationAC);
		assertNotNull(associationBC);
		
		assertTrue(associations.contains(associationAB));
		assertTrue(associations.contains(associationAC));
		assertTrue(associations.contains(associationBC));
		assertEquals(associations.size(), naryAssociationMemberEndsSize);
	}
}

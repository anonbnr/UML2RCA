package rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import exceptions.NotAnNAryAssociationException;
import rca.adaptation.association.NaryAssociationMaterializationAdaptation;
import rca.management.EcoreModelManager;
import utility.Strings;

public class NaryAssociationMaterializationAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceFileName = "naryAssociation.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = "naryAssociationMaterialized.uml";
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		Model model = (Model) manager.load(sourceURI);
		Package root = (Package) model.getPackagedElement("root");
		
		String naryAssociationName = "naryAssociation";
		String targetClassName = Strings.capitalize(naryAssociationName);
		
		Association naryAssociation = (Association) root.getPackagedElement(naryAssociationName);
		int naryAssociationMemberEndsSize = naryAssociation.getMemberEnds().size();
		Class cls = null;
		
		try {
			cls = new NaryAssociationMaterializationAdaptation(naryAssociation).getTarget();
		} catch (NotAnNAryAssociationException e) {
			e.printStackTrace();
		}
		
		manager.save(model, targetURI);
		
		cls = (Class) root.getPackagedElement(targetClassName);
		assertNotNull(cls);
		assertEquals(cls.getName(), Strings.capitalize(naryAssociation.getName()));
		assertEquals(cls.getAssociations().size(), naryAssociationMemberEndsSize);
	}
}

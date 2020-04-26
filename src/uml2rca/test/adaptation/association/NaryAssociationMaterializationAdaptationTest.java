package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import uml2rca.adaptation.association.NaryAssociationMaterializationAdaptation;
import uml2rca.exceptions.NotAnNAryAssociationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.model.management.EcoreModelManager;

public class NaryAssociationMaterializationAdaptationTest {

	@Test
	public void testTransformation() {
		String sourceFileName = "naryAssociation.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = "naryAssociationMaterialized.uml";
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		Package root = (Package) model.getPackagedElement("root");
		
		String naryAssociationName = "naryAssociation";
		Association naryAssociation = (Association) root.getPackagedElement(naryAssociationName);
		int naryAssociationMemberEndsSize = naryAssociation.getMemberEnds().size();
		Class target = null;
		
		try {
			target = new NaryAssociationMaterializationAdaptation(naryAssociation).getTarget();
		} catch (NotAnNAryAssociationException e) {
			e.printStackTrace();
		}
		
		String targetClassName = Strings.capitalize(naryAssociationName);
		target = (Class) root.getPackagedElement(targetClassName);
		
		assertNotNull(target);
		assertEquals(target.getName(), Strings.capitalize(naryAssociation.getName()));
		assertEquals(target.getAssociations().size(), naryAssociationMemberEndsSize);
		
		modelManager.saveStateAndExport(model, 
				"N-ary Association Adaptation (Materialization Solution)", targetURI);
		modelManager.displayStates();
	}
}

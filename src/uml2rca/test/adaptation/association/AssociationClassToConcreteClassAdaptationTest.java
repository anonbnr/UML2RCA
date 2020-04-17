package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import uml2rca.adaptation.association.AssociationClassToConcreteClassAdaptation;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.management.EcoreModelManager;

public class AssociationClassToConcreteClassAdaptationTest {

	@Test
	public void testTransformation() {
		String sourceFileName = "associationClass.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		
		String associationClassName = "AssocClass";
		String targetClassName = Strings.capitalize(associationClassName);
		
		AssociationClass associationClass = (AssociationClass) model.getPackagedElement(associationClassName);
		int associationClassMemberEndsSize = associationClass.getMemberEnds().size();
		int associationClassOwnedAttributesNumber = associationClass.getOwnedAttributes().size();
		Class cls = new AssociationClassToConcreteClassAdaptation(associationClass).getTarget();
		
		cls = (Class) model.getPackagedElement(targetClassName);
		assertNotNull(cls);
		assertEquals(cls.getName(), Strings.capitalize(associationClassName));
		assertEquals(cls.getOwnedAttributes().size(), associationClassOwnedAttributesNumber);
		assertEquals(cls.getAssociations().size(), associationClassMemberEndsSize);
		
		modelManager.saveStateAndExport(model, "Association Class Adaptation", targetURI);
		modelManager.displayStates();
	}
}

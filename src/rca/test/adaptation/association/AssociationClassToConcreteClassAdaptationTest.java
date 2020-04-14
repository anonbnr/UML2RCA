package rca.test.adaptation.association;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import rca.adaptation.association.AssociationClassToConcreteClassAdaptation;
import rca.management.EcoreModelManager;
import utility.Strings;

public class AssociationClassToConcreteClassAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceFileName = "associationClass.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		Model model = (Model) manager.load(sourceURI);
		
		String associationClassName = "AssocClass";
		String targetClassName = Strings.capitalize(associationClassName);
		
		AssociationClass associationClass = (AssociationClass) model.getPackagedElement(associationClassName);
		int associationClassMemberEndsSize = associationClass.getMemberEnds().size();
		Class cls = new AssociationClassToConcreteClassAdaptation(associationClass).getTarget();
		
		manager.save(model, targetURI);
		
		cls = (Class) model.getPackagedElement(targetClassName);
		assertNotNull(cls);
		assertEquals(cls.getName(), Strings.capitalize(associationClassName));
		assertEquals(cls.getAssociations().size(), associationClassMemberEndsSize);
	}
}

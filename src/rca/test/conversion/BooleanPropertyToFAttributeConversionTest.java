package rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import rca.FAttribute;
import rca.conversion.BooleanAttributeToFAttributeConversion;
import rca.management.EcoreModelManager;

public class BooleanPropertyToFAttributeConversionTest {
	
	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.load("model/test/conversion/attribute/source/attribute.uml");
		Package root = (Package) model.getPackagedElement("root");
		Class cls = (Class) root.getPackagedElement("A");
		Property booleanProp = cls.getAttributes().get(0);
		FAttribute booleanAtt = (new BooleanAttributeToFAttributeConversion(booleanProp)).getTarget();
		
		System.out.println(booleanAtt);
		
		assertEquals(booleanAtt.getName(), booleanProp.getName());
		assertTrue(booleanAtt instanceof FAttribute);
	}
}

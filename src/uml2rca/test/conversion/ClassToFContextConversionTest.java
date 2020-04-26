package uml2rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import rca.FContext;
import uml2rca.conversion.ClassToFContextConversion;
import uml2rca.model.management.EcoreModelManager;

public class ClassToFContextConversionTest {
	
	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.importModel("model/test/conversion/class/source/class.uml");
		Package root = (Package) model.getPackagedElement("root");
		Class cls = (Class) root.getPackagedElement("A");
		FContext context = (new ClassToFContextConversion(cls)).getTarget();
		
		System.out.println(context);
		System.out.println(context.getAttributeSet().getAttributes());
		
		assertEquals(context.getName(), cls.getName());
		assertTrue(context instanceof FContext);
	}
}
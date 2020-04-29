package uml2rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import rca.FContext;
import uml2rca.conversion.ClassToFContextConversion;
import uml2rca.model.management.EcoreModelManager;

public class ClassToFContextConversionTest {
	
	/* ATTRIBUTES */
	protected EcoreModelManager modelManager;
	protected String sourceFileName;
	protected String sourceURI;
	protected Model model;
	protected Package root;
	protected Class A;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializePackages();
		initializeClasses();
	}
	
	public void initializeConfigurationAndManager() {
		sourceFileName = "class.uml";
		sourceURI = "model/test/conversion/class/source/" + sourceFileName;
		
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeModel() {
		model = modelManager.getModel();
	}
	
	private void initializePackages() {
		root = (Package) model.getPackagedElement("root");
	}
	
	private void initializeClasses() {
		A = (Class) root.getPackagedElement("A");
	}
	
	/* UNIT TESTS */
	@Test
	public void testTransformation() {
		FContext context = (new ClassToFContextConversion(A)).getTarget();
		
		System.out.println(context);
		System.out.println(context.getAttributeSet().getAttributes());
		
		assertEquals(context.getName(), A.getName());
		assertTrue(context instanceof FContext);
	}
}
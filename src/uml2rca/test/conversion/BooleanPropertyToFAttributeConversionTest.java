package uml2rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.junit.Before;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import rca.FAttribute;
import uml2rca.conversion.BooleanAttributeToFAttributeConversion;
import uml2rca.model.management.EcoreModelManager;

public class BooleanPropertyToFAttributeConversionTest {
	
	/* ATTRIBUTES */
	protected EcoreModelManager modelManager;
	protected String sourceFileName;
	protected String sourceURI;
	protected Model model;
	protected Package root;
	protected Class A;
	protected Property booleanProp;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializePackages();
		initializeClasses();
	}
	
	public void initializeConfigurationAndManager() {
		sourceFileName = "attribute.uml";
		sourceURI = "model/test/conversion/attribute/source/" + sourceFileName;
		
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
		booleanProp = A.getAttributes().get(0);
	}
	
	/* UNIT TESTS */
	@Test
	public void testTransformation() {
		FAttribute booleanAtt = (new BooleanAttributeToFAttributeConversion(booleanProp)).getTarget();
		
		System.out.println(booleanAtt);
		
		assertEquals(booleanAtt.getName(), booleanProp.getName());
		assertTrue(booleanAtt instanceof FAttribute);
	}
}

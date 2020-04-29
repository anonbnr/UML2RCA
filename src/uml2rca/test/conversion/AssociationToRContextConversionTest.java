package uml2rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.junit.Before;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import rca.RContext;
import uml2rca.conversion.AssociationToRContextConversion;
import uml2rca.model.management.EcoreModelManager;

public class AssociationToRContextConversionTest {
	
	/* ATTRIBUTES */
	protected EcoreModelManager modelManager;
	protected String sourceFileName;
	protected String sourceURI;
	protected Model model;
	protected Association uses;
	protected String nameProvidedByExpert;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializeAssociations();
	}
	
	public void initializeConfigurationAndManager() {
		sourceFileName = "association.uml";
		sourceURI = "model/test/conversion/association/source/" + sourceFileName;
		
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeModel() {
		model = modelManager.getModel();
	}
	
	private void initializeAssociations() {
		uses = (Association) model.getPackagedElement("uses");
	}
	
	/* UNIT TESTS */
	@Test
	public void testTransformation() {
		nameProvidedByExpert = "nameProvidedByExpert";
		
		RContext rContextDefaultName = (new AssociationToRContextConversion(uses)).getTarget();
		RContext rContextNamed = (new AssociationToRContextConversion(uses, nameProvidedByExpert)).getTarget();
		
		System.out.println("RContext with default name: " + rContextDefaultName);
		System.out.println("RContext with name provided by expert: " + rContextNamed);
		
		assertEquals(rContextDefaultName.getName(), uses.getName());
		assertEquals(rContextNamed.getName(), nameProvidedByExpert);
		
		assertTrue(rContextDefaultName instanceof RContext);
		assertTrue(rContextNamed instanceof RContext);
	}
}

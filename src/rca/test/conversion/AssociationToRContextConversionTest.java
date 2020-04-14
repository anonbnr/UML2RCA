package rca.test.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

import rca.RContext;
import rca.conversion.AssociationToRContextConversion;
import rca.management.EcoreModelManager;

public class AssociationToRContextConversionTest {
	
	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.load("model/test/conversion/association/source/association.uml");
		Association uses = (Association) model.getPackagedElement("uses");
		
		RContext rContextDefaultName = (new AssociationToRContextConversion(uses)).getTarget();
		RContext rContextNamed = (new AssociationToRContextConversion(uses, "namedProvidedByExpert")).getTarget();
		
		System.out.println("RContext with default name: " + rContextDefaultName);
		System.out.println("RContext with name provided by expert: " + rContextNamed);
		
		assertEquals(rContextDefaultName.getName(), uses.getName());
		assertEquals(rContextNamed.getName(), "namedProvidedByExpert");
		
		assertTrue(rContextDefaultName instanceof RContext);
		assertTrue(rContextNamed instanceof RContext);
	}
}

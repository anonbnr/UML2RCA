package rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import exceptions.NotATypeException;
import rca.adaptation.association.DependencyToAssociationAdaptation;
import rca.management.EcoreModelManager;

public class DependencyToAssociationAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.load("model/test/adaptation/association/source/dependency.uml");
		Package root = (Package) model.getPackagedElement("root");
		
		Dependency dependency = (Dependency) root.getPackagedElement("dependsOn");
		EList<Association> associations = null;
		
		try {
			associations = (new DependencyToAssociationAdaptation(dependency)).getTarget();
		} catch (NotATypeException e) {
			e.printStackTrace();
		}
		
		assertEquals(associations.size(), 2);
		
		manager.save(model, "model/test/adaptation/association/target/dependency.uml");
		Association associationAToB = (Association) root.getMember("a-dependsOn-b");
		Association associationAToC = (Association) root.getMember("a-dependsOn-c");
		
		assertNotNull(associationAToB);
		assertNotNull(associationAToC);
		assertTrue(associations.contains(associationAToB));
		assertTrue(associations.contains(associationAToC));
		
		for (Property memberEnd: associationAToB.getMemberEnds())
			assertEquals(memberEnd.getAggregation(), AggregationKind.NONE_LITERAL);
		
		for (Property memberEnd: associationAToC.getMemberEnds())
			assertEquals(memberEnd.getAggregation(), AggregationKind.NONE_LITERAL);
	}
}

package uml2rca.test.adaptation.association;

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

import uml2rca.adaptation.association.DependencyToAssociationAdaptation;
import uml2rca.exceptions.NotATypeException;
import uml2rca.model.management.EcoreModelManager;

public class DependencyToAssociationAdaptationTest {

	@Test
	public void testTransformation() {
		String sourceFileName = "dependency.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		Package root = (Package) model.getPackagedElement("root");
		
		String dependencyName = "dependsOn";
		Dependency dependency = (Dependency) root.getPackagedElement(dependencyName);
		EList<Association> associations = null;
		
		try {
			associations = (new DependencyToAssociationAdaptation(dependency)).getTarget();
		} catch (NotATypeException e) {
			e.printStackTrace();
		}
		
		String associationAToBName = "a-dependsOn-b";
		String associationAToCName = "a-dependsOn-c";
		Association associationAToB = (Association) root.getMember(associationAToBName);
		Association associationAToC = (Association) root.getMember(associationAToCName);
		
		assertEquals(associations.size(), 2);
		assertNotNull(associationAToB);
		assertNotNull(associationAToC);
		assertTrue(associations.contains(associationAToB));
		assertTrue(associations.contains(associationAToC));
		
		for (Property memberEnd: associationAToB.getMemberEnds())
			assertEquals(memberEnd.getAggregation(), AggregationKind.NONE_LITERAL);
		
		for (Property memberEnd: associationAToC.getMemberEnds())
			assertEquals(memberEnd.getAggregation(), AggregationKind.NONE_LITERAL);
		
		modelManager.saveStateAndExport(model, "Dependency Adaptation", targetURI);
		modelManager.displayStates();
	}
}

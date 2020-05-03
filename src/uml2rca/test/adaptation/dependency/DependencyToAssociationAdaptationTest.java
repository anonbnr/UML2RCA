package uml2rca.test.adaptation.dependency;

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

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.dependency.DependencyToAssociationAdaptation;
import uml2rca.exceptions.NotATypeException;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class DependencyToAssociationAdaptationTest {

	@Test
	public void testTransformation() {
		String sourceFileName = "dependency.uml";
		String sourceURI = "model/test/adaptation/dependency/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/dependency/target/" + targetFileName;
		
		EcoreModelManager modelManager = null;
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
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
		
		try {
			modelManager.saveStateAndExport(targetURI, model, 
					"Dependency Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
}

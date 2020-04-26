package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import uml2rca.adaptation.association.CompositionToAssociationAdaptation;
import uml2rca.exceptions.NotABinaryAssociationException;
import uml2rca.model.management.EcoreModelManager;

public class CompositionToAssociationAdaptationTest {
	
	@Test
	public void testTransformation() {
		String sourceFileName = "composition.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		
		String compositionName = "contains";
		String targetAssociationName = compositionName;
		
		Association composition = (Association) model.getPackagedElement(compositionName);
		Association association = null;
		
		try {
			association = (new CompositionToAssociationAdaptation(composition)).getTarget();
		} catch (NotABinaryAssociationException e) {
			e.printStackTrace();
		}
		
		association = (Association) model.getPackagedElement(targetAssociationName);
		assertNotNull(association);
		
		for(Property memberEnd: association.getMemberEnds())
			assertNotEquals(memberEnd.getAggregation(), AggregationKind.COMPOSITE_LITERAL);
		
		modelManager.saveStateAndExport(model, "Composition Adaptation", targetURI);
		modelManager.displayStates();
	}
}

package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import uml2rca.adaptation.association.AggregationToAssociationAdaptation;
import uml2rca.exceptions.NotABinaryAssociationException;
import uml2rca.model.management.EcoreModelManager;

public class AggregationToAssociationAdaptationTest {
	
	@Test
	public void testTransformation() {
		String sourceFileName = "aggregation.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		
		String aggregationName = "contains";
		String targetAssociationName = aggregationName;
		
		Association aggregation = (Association) model.getPackagedElement(aggregationName);
		Association association = null;
		
		try {
			association = (new AggregationToAssociationAdaptation(aggregation)).getTarget();
		} catch (NotABinaryAssociationException e) {
			e.printStackTrace();
		}
		
		association = (Association) model.getPackagedElement(targetAssociationName);
		assertNotNull(association);
		
		for (Property memberEnd: association.getMemberEnds())
			assertNotEquals(memberEnd.getAggregation(), AggregationKind.SHARED_LITERAL);
		
		modelManager.saveStateAndExport(model, "Aggregation Adaptation", targetURI);
		modelManager.displayStates();
	}
}

package rca.test.adaptation.association;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import exceptions.NotABinaryAssociationException;
import rca.adaptation.association.CompositionToAssociationAdaptation;
import rca.management.EcoreModelManager;

public class CompositionToAssociationAdaptationTest {
	
	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceFileName = "composition.uml";
		String sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		Model model = (Model) manager.load(sourceURI);
		
		String compositionName = "contains";
		String targetAssociationName = compositionName;
		
		Association composition = (Association) model.getPackagedElement(compositionName);
		Association association = null;
		
		try {
			association = (new CompositionToAssociationAdaptation(composition)).getTarget();
		} catch (NotABinaryAssociationException e) {
			e.printStackTrace();
		}
		
		manager.save(model, targetURI);
		association = (Association) model.getPackagedElement(targetAssociationName);
		assertNotNull(association);
		
		for(Property memberEnd: association.getMemberEnds())
			assertNotEquals(memberEnd.getAggregation(), AggregationKind.COMPOSITE_LITERAL);
	}
}

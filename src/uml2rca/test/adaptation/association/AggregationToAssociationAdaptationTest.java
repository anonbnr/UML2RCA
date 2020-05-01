package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.AggregationToAssociationAdaptation;
import uml2rca.exceptions.NotABinaryAssociationException;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.test.core.UML2RCAbstractTransformationTest;

public class AggregationToAssociationAdaptationTest extends UML2RCAbstractTransformationTest {
	
	/* ATTRIBUTES */
	protected String sourceAggregationName;
	protected Association sourceAggregation;
	protected AggregationToAssociationAdaptation transformation;
	protected String targetAssociationName;
	protected Association targetAssociation;	

	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "aggregation.uml";
		sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		targetFileName = sourceFileName;
		targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initializeModel() {
		model = modelManager.getModel();
	}

	@Override
	public void initializePackages() {

	}

	@Override
	public void initializeClasses() {

	}

	@Override
	public void initializeAssociations() {

	}

	@Override
	public void preTransformationInput() {
		sourceAggregationName = "contains";
		sourceAggregation = (Association) model.getPackagedElement(sourceAggregationName);
	}

	@Override
	public void transformation() {
		try {
			transformation = new AggregationToAssociationAdaptation(sourceAggregation);
			targetAssociation = transformation.getTarget();
		} catch (NotABinaryAssociationException e) {
			e.printStackTrace();
		}
		
		transformationStateDescription = "Aggregation Adaptation";
	}

	@Override
	public void postTransformationAssertions() {
		targetAssociationName = sourceAggregationName;
		
		assertEquals(targetAssociation, model.getPackagedElement(targetAssociationName));
		
		for (Property memberEnd: targetAssociation.getMemberEnds())
			assertNotEquals(memberEnd.getAggregation(), AggregationKind.SHARED_LITERAL);
	}
}

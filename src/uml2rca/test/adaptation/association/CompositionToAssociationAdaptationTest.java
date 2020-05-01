package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.CompositionToAssociationAdaptation;
import uml2rca.exceptions.NotABinaryAssociationException;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.test.core.UML2RCAbstractTransformationTest;

public class CompositionToAssociationAdaptationTest extends UML2RCAbstractTransformationTest {
	
	/* ATTRIBUTES */
	protected String sourceCompositionName;
	protected Association sourceComposition;
	protected CompositionToAssociationAdaptation transformation;
	protected String targetAssociationName;
	protected Association targetAssociation;

	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "composition.uml";
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
		sourceCompositionName = "contains";
		sourceComposition = (Association) model.getPackagedElement(sourceCompositionName);
	}

	@Override
	public void transformation() {
		try {
			transformation = new CompositionToAssociationAdaptation(sourceComposition);
			targetAssociation = transformation.getTarget();
		} catch (NotABinaryAssociationException e) {
			e.printStackTrace();
		}
		
		transformationStateDescription = "Composition Adaptation";
	}

	@Override
	public void postTransformationAssertions() {
		targetAssociationName = sourceCompositionName;
		
		assertEquals(targetAssociation, model.getPackagedElement(targetAssociationName));
		
		for (Property memberEnd: targetAssociation.getMemberEnds())
			assertNotEquals(memberEnd.getAggregation(), AggregationKind.COMPOSITE_LITERAL);
	}
}

package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptation;
import uml2rca.exceptions.NotABidirectionalAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.test.core.UML2RCAbstractTransformationTest;

public class BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest
		extends UML2RCAbstractTransformationTest {
	
	/* ATTRIBUTES */
	protected Package root;
	protected Class A;
	protected Class B;
	protected Association sourceBidirectionalAssociation;
	protected String sourceBidirectionalAssociationName;
	protected Package sourceBidirectionalAssociationPackage;
	protected Map<Type, Property> sourceBidirectionalAssociationMemberEnds;
	protected BidirectionalAssociationToUnidirectionalAssociationsAdaptation transformation;
	protected List<String> targetAssociationsNames;
	protected List<Association> targetAssociations;

	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "bidirectional.uml";
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
		root = (Package) model.getPackagedElement("root");
	}

	@Override
	public void initializeClasses() {
		A = (Class) root.getPackagedElement("A");
		B = (Class) root.getPackagedElement("B");
	}

	@Override
	public void initializeAssociations() {
		
	}

	@Override
	public void preTransformationInput() {
		sourceBidirectionalAssociationName = "bidirectional";
		sourceBidirectionalAssociation = (Association) model.getPackagedElement(sourceBidirectionalAssociationName);
		sourceBidirectionalAssociationPackage = sourceBidirectionalAssociation.getPackage();
		
		sourceBidirectionalAssociationMemberEnds = new Hashtable<>();
		sourceBidirectionalAssociation.getMemberEnds()
		.stream()
		.forEach(memberEnd -> sourceBidirectionalAssociationMemberEnds.put(memberEnd.getType(), memberEnd));
	}

	@Override
	public void transformation() {
		transformationStateDescription = "Bidirectional Association Adaptation";
		
		try {
			transformation = new BidirectionalAssociationToUnidirectionalAssociationsAdaptation(
					sourceBidirectionalAssociation);
		} catch (NotABidirectionalAssociationException e) {
			e.printStackTrace();
		}
		
		targetAssociations = transformation.getTarget();
	}

	@Override
	public void postTransformationAssertions() {
		validateAssociations();
		validatePostTransformationClean();
	}
	
	public void validateAssociations() {
		targetAssociationsNames = Arrays.asList(
				"first-" + sourceBidirectionalAssociationName,
				"second-" + sourceBidirectionalAssociationName);

		targetAssociations
		.stream()
		.forEach(association -> {
			assertEquals(association.getPackage(), sourceBidirectionalAssociationPackage);
			assertTrue(targetAssociationsNames.contains(association.getName()));
			assertTrue(Associations.isUnidirectional(association));
			
			association.getMemberEnds()
			.stream()
			.forEach(memberEnd -> {
				assertTrue(sourceBidirectionalAssociationMemberEnds.containsKey(memberEnd.getType()));
				assertEquals(memberEnd.getName(), 
						sourceBidirectionalAssociationMemberEnds.get(memberEnd.getType()).getName());
				assertEquals(memberEnd.getLower(), 
						sourceBidirectionalAssociationMemberEnds.get(memberEnd.getType()).getLower());
				assertEquals(memberEnd.getUpper(), 
						sourceBidirectionalAssociationMemberEnds.get(memberEnd.getType()).getUpper());
				assertEquals(memberEnd.getAggregation(), 
						sourceBidirectionalAssociationMemberEnds.get(memberEnd.getType()).getAggregation());
			});
		});
	}
	
	protected void validatePostTransformationClean() {
		assertFalse(sourceBidirectionalAssociationPackage
				.getPackagedElements().contains(sourceBidirectionalAssociation));
	}
}

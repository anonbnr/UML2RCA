package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Property;

import uml2rca.adaptation.association.NaryAssociationMaterializationAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.extensions.utility.Strings;

public class NaryAssociationMaterializationAdaptationTest extends AbstractNaryAssociationAdaptationTest<Class> {
	
	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		super.initializeConfigurationAndManager();
		
		targetFileName = "naryAssociationMaterialized.uml";
		targetURI = "model/test/adaptation/association/target/" + targetFileName;
	}

	@Override
	public void initializeAssociations() {
		
	}

	@Override
	public void transformation() {
		transformationStateDescription = "N-ary Association Adaptation (Forgotten association solution)";
		
		try {
			transformation = new NaryAssociationMaterializationAdaptation(sourceNaryAssociation);
		} catch (NotAnNaryAssociationException e) {
			e.printStackTrace();
		}
		
		target = transformation.getTarget();
	}

	@Override
	public void postTransformationAssertions() {
		validateConcreteClass();
		validateAssociations();
		validatePostTransformationClean();
	}
	
	public void validateConcreteClass() {
		targetName = Strings.capitalize(sourceNaryAssociationName);
		
		assertEquals(target.getPackage(), sourceNaryAssociationPackage);
		
		assertEquals(target.getAssociations().size(), transformation.getAssociations().size());
		target.getAssociations()
		.stream()
		.forEach(association -> 
			assertTrue(transformation.getAssociations().contains(association)));
	}
	
	public void validateAssociations() {
		
		assertEquals(transformation.getAssociations().size(), sourceNaryAssociationMemberEnds.size());
		
		transformation.getAssociations()
		.stream()
		.forEach(association -> {
			assertEquals(association.getPackage(), sourceNaryAssociationPackage);
			
			association.getMemberEnds()
			.stream()
			.forEach(memberEnd -> {
				if (memberEnd.getType() == target)
					validateTargetMemberEnd(association, memberEnd);
				
				else {
					assertEquals(association.getName(), memberEnd.getType().getName() + "-" + targetName);
					validateNonTargetMemberEnd(association, memberEnd);
				}
			});
		});
	}

	protected void validateTargetMemberEnd(Association association, Property targetEnd) {
		
		for (Property memberEnd: association.getMemberEnds()) {
			if (sourceNaryAssociationMemberEnds.containsKey(memberEnd.getType())) {
				assertEquals(targetEnd.getName(), 
						Strings.decapitalize(targetName) + "-" + memberEnd.getType().getName());
				assertEquals(targetEnd.isNavigable(), 
						NaryAssociationMaterializationAdaptation
						.getTargetClassMemberEndNavigability(association.getMemberEnds(),
								sourceNaryAssociationMemberEnds.get(memberEnd.getType())));
				assertEquals(targetEnd.getLower(), 
						sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getLower());
				
				assertEquals(targetEnd.getUpper(), 
						sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getUpper());
			}
		}
		
		assertEquals(targetEnd.getAggregation(), AggregationKind.NONE_LITERAL);
	}
	
	protected void validateNonTargetMemberEnd(Association association, Property nonTargetEnd) {
		assertEquals(nonTargetEnd.getName(), sourceNaryAssociationMemberEnds.get(nonTargetEnd.getType()).getName());
		assertEquals(nonTargetEnd.getLower(), 1);
		assertEquals(nonTargetEnd.getUpper(), 1);
		assertEquals(nonTargetEnd.getAggregation(), sourceNaryAssociationMemberEnds.get(nonTargetEnd.getType()).getAggregation());
		assertEquals(nonTargetEnd.isNavigable(), sourceNaryAssociationMemberEnds.get(nonTargetEnd.getType()).isNavigable());
	}
}

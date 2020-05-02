package uml2rca.test.adaptation.association;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Property;
import org.hamcrest.CoreMatchers;

import uml2rca.adaptation.association.NaryAssociationToBinaryAssociationsAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;

public class NaryAssociationToBinaryAssociationsAdaptationTest extends AbstractNaryAssociationAdaptationTest<List<Association>> {

	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		super.initializeConfigurationAndManager();
		
		targetFileName = "naryAssociationForgotten.uml";
		targetURI = "model/test/adaptation/association/target/" + targetFileName;
	}
	
	@Override
	public void initializeAssociations() {
		
	}

	@Override
	public void transformation() {
		transformationStateDescription = "N-ary Association Adaptation (Forgotten association solution)";
		
		try {
			transformation = new NaryAssociationToBinaryAssociationsAdaptation(sourceNaryAssociation);
		} catch (NotAnNaryAssociationException e) {
			e.printStackTrace();
		}
		
		target = transformation.getTarget();
	}

	@Override
	public void postTransformationAssertions() {
		validateAssociations();
		validatePostTransformationClean();
	}
	
	public void validateAssociations() {
		
		assertEquals(transformation.getAssociations().size(), sourceNaryAssociationMemberEnds.size());
		
		transformation.getAssociations()
		.stream()
		.forEach(association -> {
			assertEquals(association.getPackage(), sourceNaryAssociationPackage);
			validateTargetAssociationName(association);
			
			association.getMemberEnds()
			.stream()
			.forEach(memberEnd -> validateTargetAssociationMemberEnd(association, memberEnd));
		});
	}

	protected void validateTargetAssociationName(Association association) {
		List<String> memberEndNames = association.getMemberEnds()
		.stream()
		.map(Property::getName)
		.collect(Collectors.toList());
		
		assertThat(association.getName(), CoreMatchers.either(
				CoreMatchers.is(memberEndNames.get(0) + "-" + sourceNaryAssociationName + "-" + memberEndNames.get(1)))
				.or(CoreMatchers.is(memberEndNames.get(1) + "-" + sourceNaryAssociationName + "-" + memberEndNames.get(0))));
	}

	protected void validateTargetAssociationMemberEnd(Association association, Property memberEnd) {
		assertEquals(memberEnd.getName(), sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getName());
		assertEquals(memberEnd.getLower(), sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getLower());
		assertEquals(memberEnd.getUpper(), sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getUpper());
		assertEquals(memberEnd.getAggregation(), sourceNaryAssociationMemberEnds.get(memberEnd.getType()).getAggregation());
		assertEquals(memberEnd.isNavigable(), sourceNaryAssociationMemberEnds.get(memberEnd.getType()).isNavigable());
	}
}

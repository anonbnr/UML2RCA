package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.AssociationClassToConcreteClassAdaptation;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.test.core.UML2RCAbstractTransformationTest;

public class AssociationClassToConcreteClassAdaptationTest extends UML2RCAbstractTransformationTest {

	/* ATTRIBUTES */
	protected Package root;
	protected Class A;
	protected Class B;
	protected AssociationClass sourceAssociationClass;
	protected String sourceAssociationClassName;
	protected Package sourceAssociationClassPackage;
	protected Map<String, Type> sourceAssociationClassAttributes;
	protected Map<Type, Property> sourceAssociationClassMemberEnds;
	protected AssociationClassToConcreteClassAdaptation transformation;
	protected String targetClassName;
	protected Class targetClass;
	
	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "associationClass.uml";
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
		sourceAssociationClassName = "AssocClass";
		sourceAssociationClass = (AssociationClass) model.getPackagedElement(sourceAssociationClassName);
		sourceAssociationClassPackage = sourceAssociationClass.getPackage();

		sourceAssociationClassAttributes = new Hashtable<>();		
		sourceAssociationClass.getOwnedAttributes()
		.stream()
		.forEach(attribute -> 
			sourceAssociationClassAttributes.put(attribute.getName(), attribute.getType()));
		
		sourceAssociationClassMemberEnds = new Hashtable<>();
		sourceAssociationClass.getMemberEnds()
		.stream()
		.forEach(memberEnd -> sourceAssociationClassMemberEnds.put(memberEnd.getType(), memberEnd));
	}

	@Override
	public void transformation() {
		transformationStateDescription = "Association Class Adaptation";
		transformation = new AssociationClassToConcreteClassAdaptation(sourceAssociationClass);
		targetClass = transformation.getTarget();
	}

	@Override
	public void postTransformationAssertions() {
		validateConcreteClass();
		validateAssociations();
		validatePostTransformationClean();
	}

	public void validateConcreteClass() {
		targetClassName = Strings.capitalize(sourceAssociationClassName);
		
		assertEquals(targetClass.getPackage(), sourceAssociationClassPackage);
		
		assertEquals(targetClass.getOwnedAttributes().size(), sourceAssociationClassAttributes.size());
		targetClass.getOwnedAttributes()
		.stream()
		.forEach(attribute -> {
			assertTrue(sourceAssociationClassAttributes.containsKey(attribute.getName()));
			assertEquals(sourceAssociationClassAttributes.get(attribute.getName()), attribute.getType());
		});
		
		assertEquals(targetClass.getAssociations().size(), transformation.getAssociations().size());
		targetClass.getAssociations()
		.stream()
		.forEach(association -> 
			assertTrue(transformation.getAssociations().contains(association)));
	}
	
	public void validateAssociations() {
		
		assertEquals(transformation.getAssociations().size(), sourceAssociationClassMemberEnds.size());
		
		transformation.getAssociations()
		.stream()
		.forEach(association -> {
			assertEquals(association.getPackage(), sourceAssociationClassPackage);
			
			association.getMemberEnds()
			.stream()
			.forEach(memberEnd -> {
				if (memberEnd.getType() == targetClass)
					validateTargetMemberEnd(association, memberEnd);
				
				else
					validateNonTargetMemberEnd(association, memberEnd);
			});
		});
	}

	protected void validateTargetMemberEnd(Association association, Property targetEnd) {
		if (association.getEndTypes().contains(A)) {
			assertEquals(targetEnd.getName(), 
					Strings.decapitalize(targetClassName) + "-" + A.getName());
			assertEquals(targetEnd.getLower(), sourceAssociationClassMemberEnds.get(B).getLower());
			assertEquals(targetEnd.getUpper(), sourceAssociationClassMemberEnds.get(B).getUpper());
			assertEquals(targetEnd.isNavigable(), sourceAssociationClassMemberEnds.get(B).isNavigable());
		}
		
		else if (association.getEndTypes().contains(B)) {
			assertEquals(targetEnd.getName(), 
					Strings.decapitalize(targetClassName) + "-" + B.getName());
			assertEquals(targetEnd.getLower(), sourceAssociationClassMemberEnds.get(A).getLower());
			assertEquals(targetEnd.getUpper(), sourceAssociationClassMemberEnds.get(A).getUpper());
			assertEquals(targetEnd.isNavigable(), sourceAssociationClassMemberEnds.get(A).isNavigable());
		}
		
		assertEquals(targetEnd.getAggregation(), AggregationKind.NONE_LITERAL);
	}
	
	protected void validateNonTargetMemberEnd(Association association, Property nonTargetEnd) {
		assertEquals(association.getName(), nonTargetEnd.getType().getName() + "-" + targetClassName);
		assertEquals(nonTargetEnd.getName(), sourceAssociationClassMemberEnds.get(nonTargetEnd.getType()).getName());
		assertEquals(nonTargetEnd.getLower(), 1);
		assertEquals(nonTargetEnd.getUpper(), 1);
		assertEquals(nonTargetEnd.getAggregation(), sourceAssociationClassMemberEnds.get(nonTargetEnd.getType()).getAggregation());
	}
	
	protected void validatePostTransformationClean() {
		assertFalse(sourceAssociationClassPackage
				.getPackagedElements().contains(sourceAssociationClass));
	}
}

package uml2rca.test.adaptation.association;

import static org.junit.Assert.assertFalse;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.AbstractNaryAssociationAdaptation;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.test.core.UML2RCAbstractTransformationTest;

public abstract class AbstractNaryAssociationAdaptationTest<T> extends UML2RCAbstractTransformationTest {

	/* ATTRIBUTES */
	protected Package root;
	protected Class A;
	protected Class B;
	protected Class C;
	protected Association sourceNaryAssociation;
	protected String sourceNaryAssociationName;
	protected Package sourceNaryAssociationPackage;
	protected Map<Type, Property> sourceNaryAssociationMemberEnds;
	protected AbstractNaryAssociationAdaptation<T> transformation;
	protected String targetName;
	protected T target;

	/* METHODS */
	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "naryAssociation.uml";
		sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		
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
		C = (Class) root.getPackagedElement("C");
	}

	@Override
	public void preTransformationInput() {
		sourceNaryAssociationName = "naryAssociation";
		sourceNaryAssociation = (Association) root.getPackagedElement(sourceNaryAssociationName);
		sourceNaryAssociationPackage = sourceNaryAssociation.getPackage();
		
		sourceNaryAssociationMemberEnds = new Hashtable<>();
		sourceNaryAssociation.getMemberEnds()
		.stream()
		.forEach(memberEnd -> sourceNaryAssociationMemberEnds.put(memberEnd.getType(), memberEnd));
	}
	
	protected void validatePostTransformationClean() {
		assertFalse(sourceNaryAssociationPackage
				.getPackagedElements().contains(sourceNaryAssociation));
	}
}

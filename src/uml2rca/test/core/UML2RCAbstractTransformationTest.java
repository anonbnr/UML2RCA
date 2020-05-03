package uml2rca.test.core;

import org.eclipse.uml2.uml.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public abstract class UML2RCAbstractTransformationTest {
	
	/* ATTRIBUTES */
	protected String sourceFileName;
	protected String sourceURI;
	protected String targetFileName;
	protected String targetURI;
	protected EcoreModelManager modelManager;
	protected Model model;
	protected String transformationStateDescription;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializePackages();
		initializeClasses();
		initializeAssociations();
	}
	
	@Test
	public void testTransformation() {
		preTransformationInput();
		transformation();
		postTransformationAssertions();
	}
	
	@After
	public void tearDown() {
		try {
			modelManager.saveStateAndExport(targetURI, model, transformationStateDescription, EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		modelManager.displayStates();
	}
	
	public abstract void initializeConfigurationAndManager();
	public abstract void initializeModel();
	public abstract void initializePackages();
	public abstract void initializeClasses();
	public abstract void initializeAssociations();
	public abstract void preTransformationInput();
	public abstract void transformation();
	public abstract void postTransformationAssertions();
}

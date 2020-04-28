package uml2rca.test.adaptation.dependency;

import java.util.List;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;

import uml2rca.adaptation.dependency.DependencyWithAbstractMembersAdaptation;
import uml2rca.exceptions.NotADependencyWithAnAbstractMemberException;
import uml2rca.exceptions.NotATypeException;
import uml2rca.model.management.EcoreModelManager;

public class DependencyWithAbstractMembersAdaptationTest {
	
	/* ATTRIBUTES */
	protected EcoreModelManager modelManager;
	protected String sourceFileName;
	protected String sourceURI;
	protected String targetFileName;
	protected String targetURI;
	protected Model model;
	protected Package package1;
	protected Package package1Point1;
	protected Package package2;
	protected Class documentPlatform;
	protected Class document;
	protected Class report;
	protected Class thesis;
	protected Class article;
	protected Class journalArticle;
	protected Class inProceedings;
	protected Class journalArticleInProceedings;
	protected Dependency dependsOnPlatform;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializePackages();
		initializeClasses();
		initializeDependencies();
	}
	
	public void initializeConfigurationAndManager() {
		sourceFileName = "dependencyWithAbstractMembers.uml";
		sourceURI = "model/test/adaptation/dependency/source/" + sourceFileName;
		targetFileName = sourceFileName;
		targetURI = "model/test/adaptation/dependency/target/" + targetFileName;
		modelManager = new EcoreModelManager(sourceURI);
	}
	
	private void initializeModel() {
		model = modelManager.getModel();
	}
	
	private void initializePackages() {
		package1 = (Package) model.getPackagedElement("package1");
		package1Point1 = (Package) package1.getPackagedElement("package1.1");
		package2 = (Package) model.getPackagedElement("package2");
	}
	
	private void initializeClasses() {
		documentPlatform = (Class) package1.getPackagedElement("DocumentPlatform");
		document = (Class) package1.getPackagedElement("Document");
		report = (Class) package1.getPackagedElement("Report");
		thesis = (Class) package1Point1.getPackagedElement("Thesis");
		article = (Class) package2.getPackagedElement("Article");
		journalArticle = (Class) package2.getPackagedElement("JournalArticle");
		inProceedings = (Class) package2.getPackagedElement("InProceedings");
		journalArticleInProceedings = (Class) package2.getPackagedElement("JournalArticleInProceedings");
	}
	
	private void initializeDependencies() {
		dependsOnPlatform = (Dependency) package1.getPackagedElement("dependsOnPlatform");
	}

	/* UNIT TESTS */
	@Test
	public void testTransformation() {
		List<Dependency> newOwnedDependencies = null;
		
		try {
			newOwnedDependencies = new DependencyWithAbstractMembersAdaptation(dependsOnPlatform).getTarget();
		} catch (NotADependencyWithAnAbstractMemberException | NotATypeException e) {
			e.printStackTrace();
		}
		
		modelManager.saveStateAndExport(model, "Dependencies with abstract members Adaptation", targetURI);
		modelManager.displayStates();
	}
}

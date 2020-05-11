package uml2rca.test.adaptation.association;

import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Before;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.association.AssociationClassWithAbstractMembersAdaptation;
import uml2rca.adaptation.association.AssociationWithAbstractMembersAdaptation;
import uml2rca.exceptions.NotAnAssociationClassException;
import uml2rca.exceptions.NotAnAssociationWithAnAbstractMemberException;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class AssociationWithAbstractMembersAdaptationTest {
	
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
	protected Association describedIn;
	protected Association basedOn;
	protected Association attacks;
	protected AssociationClass ImportedTo;
	
	/* METHODS */
	@Before
	public void setUp() {
		initializeConfigurationAndManager();
		initializeModel();
		initializePackages();
		initializeClasses();
		initializeAssociations();
	}
	
	public void initializeConfigurationAndManager() {
		sourceFileName = "associationWithAbstractMembers.uml";
		sourceURI = "model/test/adaptation/association/source/" + sourceFileName;
		targetFileName = sourceFileName;
		targetURI = "model/test/adaptation/association/target/" + targetFileName;
		
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
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
	
	private void initializeAssociations() {
		describedIn = (Association) package1.getPackagedElement("describedIn");
		basedOn = (Association) model.getPackagedElement("basedOn");
		attacks = (Association) model.getPackagedElement("attacks");
		ImportedTo = (AssociationClass) model.getPackagedElement("ImportedTo");
	}

	/* UNIT TESTS */
	@Test
	public void testTransformation() {
		List<Association> newOwnedAssociations = null;
		
		try {
			newOwnedAssociations = new AssociationWithAbstractMembersAdaptation(describedIn).getTarget();
		} catch (NotAnAssociationWithAnAbstractMemberException e) {
			e.printStackTrace();
		}
		
		try {
			newOwnedAssociations = new AssociationWithAbstractMembersAdaptation(basedOn).getTarget();
		} catch (NotAnAssociationWithAnAbstractMemberException e) {
			e.printStackTrace();
		}
		
		try {
			newOwnedAssociations = new AssociationWithAbstractMembersAdaptation(attacks).getTarget();
		} catch (NotAnAssociationWithAnAbstractMemberException e) {
			e.printStackTrace();
		}
		
		try {
			newOwnedAssociations = new AssociationClassWithAbstractMembersAdaptation(ImportedTo).getTarget();
		} catch (NotAnAssociationClassException | NotAnAssociationWithAnAbstractMemberException e) {
			e.printStackTrace();
		}
		
		try {
			modelManager.saveStateAndExport(targetURI, model, "Associations with abstract members Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		modelManager.displayStates();
	}
}

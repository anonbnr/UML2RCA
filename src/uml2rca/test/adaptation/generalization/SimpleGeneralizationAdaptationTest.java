package uml2rca.test.adaptation.generalization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.generalization.SimpleGeneralizationAdaptation;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.AttributeConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAttributeVisitor;
import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class SimpleGeneralizationAdaptationTest {
	
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
	protected Package package3;
	protected Class document;
	protected Class report;
	protected Class thesis;
	protected Class article;
	protected Class journalArticle;
	protected Class inProceedings;
	protected Class publisher;
	protected Class dataSource;
	protected Class author;
	protected Class reviewer;
	protected Class editor;
	protected Class page;
	protected Association describedIn;
	protected Association edits;
	protected Association writes;
	protected Association reviews;
	protected Association publishes;
	protected Association providesData;
	
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
		sourceFileName = "simpleGeneralization.uml";
		sourceURI = "model/test/adaptation/generalization/source/" + sourceFileName;
		targetFileName = sourceFileName;
		targetURI = "model/test/adaptation/generalization/target/" + targetFileName;
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
		package3 = (Package) model.getPackagedElement("package3");
	}
	
	private void initializeClasses() {
		document = (Class) package1.getPackagedElement("Document");
		report = (Class) package1.getPackagedElement("Report");
		thesis = (Class) package1Point1.getPackagedElement("Thesis");
		article = (Class) package2.getPackagedElement("Article");
		journalArticle = (Class) package2.getPackagedElement("JournalArticle");
		inProceedings = (Class) package2.getPackagedElement("InProceedings");
		publisher = (Class) package3.getPackagedElement("Publisher");
		dataSource = (Class) package3.getPackagedElement("DataSource");
		author = (Class) package3.getPackagedElement("Author");
		reviewer = (Class) package3.getPackagedElement("Reviewer");
		editor = (Class) package3.getPackagedElement("Editor");
		page = (Class) package3.getPackagedElement("Page");
	}
	
	private void initializeAssociations() {
		describedIn = (Association) model.getPackagedElement("describedIn");
		edits = (Association) model.getPackagedElement("edits");
		writes = (Association) model.getPackagedElement("writes");
		reviews = (Association) model.getPackagedElement("reviews");
		publishes = (Association) model.getPackagedElement("publishes");
		providesData = (Association) model.getPackagedElement("providesData");
	}

	/* UNIT TESTS */
	@Ignore
	@Test
	public void testPackageAndName() {
		Package leafClassPackage = package2;
		String leafClassName = "JournalArticle";
		Package chosenClassPackage = package2;
		String chosenClassName = "Article";
		String targetClassName = chosenClassName;
		Class leafClass = (Class)  leafClassPackage.getPackagedElement(leafClassName);
		Class chosenClass = (Class) chosenClassPackage.getPackagedElement(chosenClassName);
		Class targetClass = null;
		SimpleGeneralizationAdaptation adaptation = null;
		
		try {
			adaptation = new 
					SimpleGeneralizationAdaptation(leafClass, chosenClass);
		} catch (NotALeafInGeneralizationHierarchyException | NotAValidLevelForGeneralizationAdaptationException e) {
			e.printStackTrace();
		}
		
		targetClass = (Class) chosenClassPackage.getPackagedElement(targetClassName);
		
		assertNotNull(targetClass);
		assertEquals(targetClass.getName(), chosenClassName);
		assertEquals(targetClass.getPackage(), chosenClassPackage);
		
		try {
			modelManager.saveStateAndExport(targetURI, model, 
					"Simple Generalization Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
	
	@Test
	public void testAttributes() {
		Package leafClassPackage = package2;
		String leafClassName = "JournalArticle";
		Package chosenClassPackage = package2;
		String chosenClassName = "JournalArticle";
		String targetClassName = chosenClassName;
		Class leafClass = (Class)  leafClassPackage.getPackagedElement(leafClassName);
		Class chosenClass = (Class) chosenClassPackage.getPackagedElement(chosenClassName);
		Class targetClass = null;
		SimpleGeneralizationAdaptation adaptation = null; 
		
		try {
			adaptation = new 
					SimpleGeneralizationAdaptation(leafClass, chosenClass);
		} catch (NotALeafInGeneralizationHierarchyException | NotAValidLevelForGeneralizationAdaptationException e) {
			e.printStackTrace();
		}
		
		targetClass = (Class) chosenClassPackage.getPackagedElement(targetClassName);
		assertNotNull(targetClass);
		
		List<String> targetAttributeNames = targetClass.getOwnedAttributes()
				.stream()
				.map(Property::getName)
				.collect(Collectors.toList());
		
		for (Property attribute: chosenClass.getOwnedAttributes())
			assertTrue(targetAttributeNames.contains(attribute.getName()));
		
		for (Class superClass: adaptation.getVisitableSource().getSuperClasses())
			for (Property attribute: superClass.getOwnedAttributes())
				assertTrue(targetAttributeNames.contains(attribute.getName()));
		
		for (Class subClass: adaptation.getVisitableSource().getSubClasses())
			for (Property attribute: subClass.getOwnedAttributes()) {
				for (GeneralizationAdaptationAttributeVisitor attributeVisitor: adaptation.getAttributeVisitors()) {
					
					if (attributeVisitor.getConflictStrategyType() == AttributeConflictResolutionStrategyType.DEFAULT_RENAME) {
						assertTrue(
								targetAttributeNames.contains(attribute.getName())
								||
								targetAttributeNames.contains(Strings.decapitalize(subClass.getName()) 
										+ Strings.capitalize(attribute.getName()))
								);
					}
					
					else if (attributeVisitor.getConflictStrategyType() == AttributeConflictResolutionStrategyType.EXPERT_RENAME) {
						assertTrue(
								targetAttributeNames.contains(attribute.getName())
								||
								(targetAttributeNames.contains("expertNameProvidedForOriginallyOwnedAttribute")
										&& targetAttributeNames.contains("expertNameProvidedForConflictingAttribute"))
								);
					}
					
					else if (attributeVisitor.getConflictStrategyType() == AttributeConflictResolutionStrategyType.DISCARD)
						assertTrue(targetAttributeNames.contains(attribute.getName()));
				}
			}
		
		try {
			modelManager.saveStateAndExport(targetURI, model, 
					"Simple Generalization Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
	
	@Ignore
	@Test(expected = NotALeafInGeneralizationHierarchyException.class)
	public void testNotALeafInGeneralizationHierarchyException() 
			throws NotALeafInGeneralizationHierarchyException, 
			NotAValidLevelForGeneralizationAdaptationException {
		
		Package leafClassPackage = package2;
		String leafClassName = "Article";
		Package chosenClassPackage = package2;
		String chosenClassName = "Article";
		String targetClassName = chosenClassName;
		Class leafClass = (Class)  leafClassPackage.getPackagedElement(leafClassName);
		Class chosenClass = (Class) chosenClassPackage.getPackagedElement(chosenClassName);
		Class targetClass = null;
		SimpleGeneralizationAdaptation adaptation = new 
				SimpleGeneralizationAdaptation(leafClass, chosenClass);
	}
	
	@Ignore
	@Test(expected = NotAValidLevelForGeneralizationAdaptationException.class)
	public void testNotAValidLevelForGeneralizationAdaptationException() 
			throws NotALeafInGeneralizationHierarchyException, 
			NotAValidLevelForGeneralizationAdaptationException {
		
		Package leafClassPackage = package2;
		String leafClassName = "JournalArticle";
		Package chosenClassPackage = package1Point1;
		String chosenClassName = "Thesis";
		String targetClassName = chosenClassName;
		Class leafClass = (Class)  leafClassPackage.getPackagedElement(leafClassName);
		Class chosenClass = (Class) chosenClassPackage.getPackagedElement(chosenClassName);
		Class targetClass = null;
		SimpleGeneralizationAdaptation adaptation = new 
				SimpleGeneralizationAdaptation(leafClass, chosenClass);
	}
}

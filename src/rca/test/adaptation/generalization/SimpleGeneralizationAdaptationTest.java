package rca.test.adaptation.generalization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import rca.adaptation.generalization.SimpleGeneralizationAdaptation;
import rca.management.EcoreModelManager;

public class SimpleGeneralizationAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceFileName = "simpleGeneralization.uml";
		String sourceURI = "model/test/adaptation/generalization/source/" + sourceFileName;
		String targetFileName = sourceFileName;
		String targetURI = "model/test/adaptation/generalization/target/" + targetFileName;
		
		Model model = (Model) manager.load(sourceURI);
		Package package1 = (Package) model.getPackagedElement("package1");
		Package package1Point1 = (Package) package1.getPackagedElement("package1.1");
		Package package2 = (Package) model.getPackagedElement("package2");
		
		String chosenClassName = "Document";
		String targetClassName = chosenClassName;
		
//		Class Document = (Class) package1.getPackagedElement("Document");
		Class Report = (Class) package1.getPackagedElement("Report");
		Class Thesis = (Class) package1Point1.getPackagedElement("Thesis");
		Class Article = (Class) package2.getPackagedElement("Article");
		Class JournalArticle = (Class) package2.getPackagedElement("JournalArticle");
		Class InProceedings = (Class) package2.getPackagedElement("InProceedings");
		
		Class chosenClass = (Class) package1.getPackagedElement(chosenClassName);
		Package chosenClassPackage = chosenClass.getPackage();
		EList<Class> chosenClassSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(chosenClass);
		Class targetClass = null;
		
		targetClass = (new SimpleGeneralizationAdaptation(chosenClass)).getTarget();
		
		manager.save(model, targetURI);
		targetClass = (Class) package1.getPackagedElement(targetClassName);
		
		assertNotNull(targetClass);
		assertTrue(chosenClassSubClasses.contains(Report));
		assertTrue(chosenClassSubClasses.contains(Thesis));
		assertTrue(chosenClassSubClasses.contains(Article));
		assertTrue(chosenClassSubClasses.contains(JournalArticle));
		assertTrue(chosenClassSubClasses.contains(InProceedings));
		assertEquals(targetClass.getName(), chosenClassName);
		assertEquals(targetClass.getPackage(), chosenClassPackage);
	}
}

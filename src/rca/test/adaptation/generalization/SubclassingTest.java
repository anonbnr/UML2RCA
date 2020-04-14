package rca.test.adaptation.generalization;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import rca.adaptation.generalization.SimpleGeneralizationAdaptation;
import rca.management.EcoreModelManager;

public class SubclassingTest {
	
	@Test
	public void testSubclassingMethod() {
		EcoreModelManager manager = new EcoreModelManager();
		String sourceURI = "model/test/adaptation/generalization/source/simpleGeneralization.uml";
		
		Model model = (Model) manager.load(sourceURI);
		Package package1 = (Package) model.getPackagedElement("package1");
		Package package1Point1 = (Package) package1.getPackagedElement("package1.1");
		Package package2 = (Package) model.getPackagedElement("package2");
		
		Class Document = (Class) package1.getPackagedElement("Document");
		Class Report = (Class) package1.getPackagedElement("Report");
		Class Thesis = (Class) package1Point1.getPackagedElement("Thesis");
		Class Article = (Class) package2.getPackagedElement("Article");
		Class InProceedings = (Class) package2.getPackagedElement("InProceedings");
		Class JournalArticle = (Class) package2.getPackagedElement("JournalArticle");
		
		EList<Class> DocumentSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(Document);
		EList<Class> ReportSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(Report);
		EList<Class> ThesisSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(Thesis);
		EList<Class> ArticleSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(Article);
		EList<Class> InProceedingsSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(InProceedings);
		EList<Class> JournalArticleSubClasses = SimpleGeneralizationAdaptation.getAllSubclasses(JournalArticle);
		
		assertTrue(DocumentSubClasses.contains(Report));
		assertTrue(DocumentSubClasses.contains(Thesis));
		assertTrue(DocumentSubClasses.contains(Article));
		assertTrue(DocumentSubClasses.contains(InProceedings));
		assertTrue(DocumentSubClasses.contains(JournalArticle));
		
		assertTrue(ReportSubClasses.contains(Thesis));
		
		assertTrue(ArticleSubClasses.contains(InProceedings));
		assertTrue(ArticleSubClasses.contains(JournalArticle));
		
		assertTrue(ThesisSubClasses.isEmpty());
		assertTrue(InProceedingsSubClasses.isEmpty());
		assertTrue(JournalArticleSubClasses.isEmpty());
	}
}

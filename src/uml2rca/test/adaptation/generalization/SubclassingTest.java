package uml2rca.test.adaptation.generalization;

import static org.junit.Assert.assertTrue;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.junit.Test;

import uml2rca.java.uml2.uml.extensions.utility.Classes;
import uml2rca.management.EcoreModelManager;

public class SubclassingTest {
	
	@Test
	public void testSubclassingMethod() {
		String sourceFileName = "simpleGeneralization.uml";
		String sourceURI = "model/test/adaptation/generalization/source/" + sourceFileName;
		
		EcoreModelManager modelManager = new EcoreModelManager(sourceURI);
		
		Model model = modelManager.getModel();
		Package package1 = (Package) model.getPackagedElement("package1");
		Package package1Point1 = (Package) package1.getPackagedElement("package1.1");
		Package package2 = (Package) model.getPackagedElement("package2");
		
		Class Document = (Class) package1.getPackagedElement("Document");
		Class Report = (Class) package1.getPackagedElement("Report");
		Class Thesis = (Class) package1Point1.getPackagedElement("Thesis");
		Class Article = (Class) package2.getPackagedElement("Article");
		Class InProceedings = (Class) package2.getPackagedElement("InProceedings");
		Class JournalArticle = (Class) package2.getPackagedElement("JournalArticle");
		
		EList<Class> DocumentSubClasses = Classes.getAllSubclasses(Document);
		EList<Class> ReportSubClasses = Classes.getAllSubclasses(Report);
		EList<Class> ThesisSubClasses = Classes.getAllSubclasses(Thesis);
		EList<Class> ArticleSubClasses = Classes.getAllSubclasses(Article);
		EList<Class> InProceedingsSubClasses = Classes.getAllSubclasses(InProceedings);
		EList<Class> JournalArticleSubClasses = Classes.getAllSubclasses(JournalArticle);
		
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

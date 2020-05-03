package uml2rca.test.adaptation.generalization;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;

import core.model.management.NotAValidModelStateException;
import uml2rca.adaptation.generalization.MultipleGeneralizationAdaptation;
import uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy.AttributeConflictResolutionStrategyType;
import uml2rca.adaptation.generalization.visitor.GeneralizationAdaptationAttributeVisitor;
import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.model.management.EcoreModelManager;
import uml2rca.model.management.EcoreModelState;

public class MultipleGeneralizationAdaptationTest extends SimpleGeneralizationAdaptationTest {
	
	@Override
	public void initializeConfigurationAndManager() {
		sourceFileName = "multipleGeneralization.uml";
		sourceURI = "model/test/adaptation/generalization/source/" + sourceFileName;
		targetFileName = sourceFileName;
		targetURI = "model/test/adaptation/generalization/target/" + targetFileName;
		try {
			modelManager = new EcoreModelManager(sourceURI);
		} catch (InstantiationException | IllegalAccessException | NotAValidModelStateException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void testAttributes() {
		Package leafClassPackage = package2;
		String leafClassName = "JournalArticleInProceedings";
		Package chosenClassPackage = package2;
		String chosenClassName = "JournalArticleInProceedings";
		String targetClassName = chosenClassName;
		Class leafClass = (Class)  leafClassPackage.getPackagedElement(leafClassName);
		Class chosenClass = (Class) chosenClassPackage.getPackagedElement(chosenClassName);
		Class targetClass = null;
		MultipleGeneralizationAdaptation adaptation = null; 
		
		try {
			adaptation = new 
					MultipleGeneralizationAdaptation(leafClass, chosenClass);
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
			for (Property attribute: superClass.getOwnedAttributes()) {
				for (GeneralizationAdaptationAttributeVisitor attributeVisitor: adaptation.getAttributeVisitors()) {
					
					if (attributeVisitor.getConflictStrategyType() == AttributeConflictResolutionStrategyType.DEFAULT_RENAME) {
						assertTrue(
								targetAttributeNames.contains(attribute.getName())
								||
								targetAttributeNames.contains(Strings.decapitalize(superClass.getName()) 
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
					"Multiple Generalization Adaptation", EcoreModelState.class);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		modelManager.displayStates();
	}
}

package uml2rca.test.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.junit.Ignore;
import org.junit.Test;

import uml2rca.adaptation.association.CompositionToAssociationAdaptation;
import uml2rca.exceptions.NotACompositionException;

public class CompositionToAssociationAdaptationExceptionsTest extends CompositionToAssociationAdaptationTest {

	/* ATTRIBUTES */
	protected Association nonComposition;
	
	@Override
	public void initializeAssociations() {
		nonComposition = (Association) model.getPackagedElement("nonComposition");
	}
	
	@Override
	public void tearDown() {
		
	}
	
	@Ignore
	@Override
	public void testTransformation() {
		
	}
	
	@Test(expected=NotACompositionException.class)
	public void testNotACompositionException() throws NotACompositionException {
			transformation = new CompositionToAssociationAdaptation(nonComposition);
	}
}

package uml2rca.test.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.junit.Ignore;
import org.junit.Test;

import uml2rca.adaptation.association.NaryAssociationMaterializationAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;

public class NaryAssociationMaterializationAdaptationExceptionsTest extends NaryAssociationMaterializationAdaptationTest {
	
	/* ATTRIBUTES */
	protected Association nonNaryAssociation;
	
	/* METHODS */
	@Override
	public void initializeAssociations() {
		nonNaryAssociation = (Association) model.getPackagedElement("nonNaryAssociation");
	}
	
	@Ignore
	@Override
	public void testTransformation() {
		
	}
	
	@Test(expected=NotAnNaryAssociationException.class)
	public void NotAnNaryAssociationException() throws NotAnNaryAssociationException {
			transformation = new NaryAssociationMaterializationAdaptation(nonNaryAssociation);
	}
	
	@Override
	public void tearDown() {
		
	}
}

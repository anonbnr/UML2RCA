package rca.test.adaptation.association;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.Test;

import exceptions.NotABidirectionalAssociationException;
import exceptions.NotABinaryAssociationException;
import rca.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptation;
import rca.management.EcoreModelManager;

public class BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest {

	@Test
	public void testTransformation() {
		EcoreModelManager manager = new EcoreModelManager();
		Model model = (Model) manager.load("model/test/adaptation/association/source/bidirectional.uml");
		
		Association bidirectional = (Association) model.getPackagedElement("bidirectional");
		EList<Association> associations = null;
		
		try {
			associations = 
					(new BidirectionalAssociationToUnidirectionalAssociationsAdaptation
							(bidirectional)
					).getTarget();
		} catch (NotABinaryAssociationException | NotABidirectionalAssociationException e) {
			e.printStackTrace();
		}
		
		manager.save(model, "model/test/adaptation/association/target/bidirectional.uml");
		Association first = (Association) model.getMember("first-bidirectional");
		Association second = (Association) model.getMember("second-bidirectional");
		
		assertNotNull(first);
		assertNotNull(second);
		
		List<Property> firstNavigableEnds = first.getMemberEnds()
		.stream()
		.filter(end -> end.isNavigable())
		.collect(Collectors.toList());
		
		assertEquals(firstNavigableEnds.size(), 1);
		
		List<Property> secondNavigableEnds = second.getMemberEnds()
		.stream()
		.filter(end -> end.isNavigable())
		.collect(Collectors.toList());
				
		assertEquals(secondNavigableEnds.size(), 1);
	}
}

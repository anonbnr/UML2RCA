package uml2rca.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uml2rca.test.adaptation.association.AggregationToAssociationAdaptationExceptionsTest;
import uml2rca.test.adaptation.association.AggregationToAssociationAdaptationTest;
import uml2rca.test.adaptation.association.AssociationClassToConcreteClassAdaptationTest;
import uml2rca.test.adaptation.association.AssociationWithAbstractMembersAdaptationTest;
import uml2rca.test.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptationExceptionsTest;
import uml2rca.test.adaptation.association.BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest;
import uml2rca.test.adaptation.association.CompositionToAssociationAdaptationExceptionsTest;
import uml2rca.test.adaptation.association.CompositionToAssociationAdaptationTest;
import uml2rca.test.adaptation.association.NaryAssociationMaterializationAdaptationExceptionsTest;
import uml2rca.test.adaptation.association.NaryAssociationMaterializationAdaptationTest;
import uml2rca.test.adaptation.association.NaryAssociationToBinaryAssociationsAdaptationExceptionsTest;
import uml2rca.test.adaptation.association.NaryAssociationToBinaryAssociationsAdaptationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AggregationToAssociationAdaptationTest.class,
	AggregationToAssociationAdaptationExceptionsTest.class,
	AssociationClassToConcreteClassAdaptationTest.class,
	AssociationWithAbstractMembersAdaptationTest.class,
	BidirectionalAssociationToUnidirectionalAssociationsAdaptationTest.class,
	BidirectionalAssociationToUnidirectionalAssociationsAdaptationExceptionsTest.class,
	CompositionToAssociationAdaptationTest.class,
	CompositionToAssociationAdaptationExceptionsTest.class,
	NaryAssociationMaterializationAdaptationTest.class,
	NaryAssociationMaterializationAdaptationExceptionsTest.class,
	NaryAssociationToBinaryAssociationsAdaptationTest.class,
	NaryAssociationToBinaryAssociationsAdaptationExceptionsTest.class
})
public class AssociationAdaptationsTestSuite {

}

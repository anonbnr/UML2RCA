package uml2rca.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uml2rca.test.adaptation.dependency.DependencyToAssociationAdaptationTest;
import uml2rca.test.adaptation.dependency.DependencyWithAbstractMembersAdaptationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	DependencyToAssociationAdaptationTest.class,
	DependencyWithAbstractMembersAdaptationTest.class
})
public class DependencyAdaptationsTestSuite {

}

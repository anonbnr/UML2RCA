package uml2rca.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uml2rca.test.adaptation.generalization.MultipleGeneralizationAdaptationTest;
import uml2rca.test.adaptation.generalization.SimpleGeneralizationAdaptationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SimpleGeneralizationAdaptationTest.class,
	MultipleGeneralizationAdaptationTest.class
})
public class GeneralizationAdaptationsTestSuite {

}

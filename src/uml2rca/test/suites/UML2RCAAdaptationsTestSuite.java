package uml2rca.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AssociationAdaptationsTestSuite.class,
	DependencyAdaptationsTestSuite.class,
	GeneralizationAdaptationsTestSuite.class
})
public class UML2RCAAdaptationsTestSuite {

}

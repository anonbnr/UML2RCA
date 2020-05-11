package uml2rca.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import uml2rca.test.conversion.AssociationToRContextConversionTest;
import uml2rca.test.conversion.BooleanPropertyToFAttributeConversionTest;
import uml2rca.test.conversion.ClassToFContextConversionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AssociationToRContextConversionTest.class,
	BooleanPropertyToFAttributeConversionTest.class,
	ClassToFContextConversionTest.class
})
public class UML2RCAConversionsTestSuite {

}

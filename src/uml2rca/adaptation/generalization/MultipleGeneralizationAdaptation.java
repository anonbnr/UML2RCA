package uml2rca.adaptation.generalization;

import org.eclipse.uml2.uml.Class;

import uml2rca.exceptions.NotALeafInGeneralizationHierarchyException;
import uml2rca.exceptions.NotAValidLevelForGeneralizationAdaptationException;

public class MultipleGeneralizationAdaptation extends SimpleGeneralizationAdaptation {

	public MultipleGeneralizationAdaptation(Class leaf, Class choice)
			throws NotALeafInGeneralizationHierarchyException, NotAValidLevelForGeneralizationAdaptationException {
		super(leaf, choice);
	}
}

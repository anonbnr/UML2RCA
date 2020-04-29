package uml2rca.model.management;

import org.eclipse.uml2.uml.Model;

import core.model.management.AbstractModelState;

public class EcoreModelState extends AbstractModelState<Model, String> {
	
	public EcoreModelState() {}
	public EcoreModelState(Model model, String description) {
		super(model, description);
	}
}

package core.model.management;

public abstract class AbstractModelState<E, S> implements IModelState<E, S> {
	
	/* ATTRIBUTES */
	protected E model;
	protected S description;

	/* CONSTRUCTORS */
	public AbstractModelState() {}
	public AbstractModelState(E model, S description) {
		this.model = model;
		this.description = description;
	}
	
	/* METHODS */
	@Override
	public E getModel() {
		return model;
	}

	@Override
	public void setModel(E model) {
		this.model = model;
	}

	@Override
	public S getDescription() {
		return description;
	}

	@Override
	public void setDescription(S description) {
		this.description = description;
	}
}
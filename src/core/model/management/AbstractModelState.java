package core.model.management;

/**
 * an AbstractModelState generic abstract class that is used to factor 
 * the common interface and state of all concrete model state classes.<br><br>
 *  
 * It must be specialized by all concrete model state classes.
 *  
 * @author Bachar Rima
 *
 * @param <E> the type of the model state's model.
 * @param <S> the type of the model state's description.
 */
public abstract class AbstractModelState<E, S> implements IModelState<E, S> {
	
	/* ATTRIBUTES */
	/**
	 * The model component of this model state
	 */
	protected E model;
	
	/**
	 * The description component of this model state
	 */
	protected S description;

	/* CONSTRUCTORS */
	/**
	 * Creates an empty model state
	 */
	public AbstractModelState() {}
	
	/**
	 * Creates a model state having model as its model component and description as its description component
	 * @param model a model that defines the model component of this model state
	 * @param description a description that defines the description component of this model state
	 */
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
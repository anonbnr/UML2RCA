package core.model.management;

/**
 * an IModelState generic interface that defines the basic operations for all model state implementations.<br><br>
 * 
 * A model state is defined as a &lt;model, description&gt; pair, where description can be used to 
 * identify the associated model. As such, this interface provides the signatures for operations allowing 
 * to access a model state implementation's model and description.<br><br>
 * 
 * It must be implemented by all concrete model state classes.
 *  
 * @author Bachar Rima
 *
 * @param <E> the type of the model state's model.
 * @param <S> the type of the model state's description.
 */
public interface IModelState<E, S> {
	
	/* METHODS */
	/**
	 * Returns the model component of this model state
	 * @return the model component of this model state
	 */
	E getModel();
	
	/**
	 * Sets the model component of this model state
	 * @param model a value to set the model of this model state
	 */
	void setModel(E model);
	
	/**
	 * Returns the description component of this model state
	 * @return the description component of this model state
	 */
	S getDescription();
	
	/**
	 * Sets the description component of this model state
	 * @param description a value to the set the description component of this model state
	 */
	void setDescription(S description);	
}

package core.model.management;

import java.util.Collection;
import java.util.LinkedList;

/**
 * an AbstractStatefulModelManager generic abstract class that is used to factor 
 * the common interface and state of all stateful concrete model manager classes.<br><br>
 * 
 * This class also provides wrapper methods for the basic model import/export operations, 
 * allowing to handle state management accordingly upon executing any of them. 
 * By default the states of a model are stored and handled by a LinkedList.<br><br>
 * 
 * It must be specialized by all concrete model manager classes.
 *  
 * @author Bachar Rima
 * @see AbstractModelState
 *
 * @param <E> the type of the model to manage.
 * @param <S> the type of the model state's description.
 */
public abstract class AbstractStatefulModelManager<E, S> extends AbstractModelManager<E> 
	implements IStatefulModelManager<E, S> {

	/* ATTRIBUTES */
	/**
	 * The managed model's current state.
	 */
	protected AbstractModelState<E, S> currentState;
	
	/**
	 * The managed model's collection of states.
	 */
	protected Collection<AbstractModelState<E, S>> states;
	
	/* CONSTRUCTORS */
	/**
	 * Creates a model manager to manage the model associated to a specific path and initializes
	 * the collection of model states as a LinkedList. 
	 * The importation details of the model associated to the path will be explicited in the 
	 * constructors of the concrete implementations of this class.
	 * @param path the path associated to the model to be managed by this model manager 
	 */
	public AbstractStatefulModelManager(String path) {
		super(path);
		states = new LinkedList<>();		
	}
	
	/**
	 * Creates a model manager to manage the model associated to a specific path and initializes
	 * the collection of model states associated with this model manager's managed model using 
	 * the provided collection of model states. The importation details of the model associated 
	 * to the path will be explicited in the constructors of the concrete implementations of this class.
	 * @param path the path associated to the model to be managed by this model manager 
	 * @param states the value initializing the states collection associated with this model manager's 
	 * managed model
	 */
	public AbstractStatefulModelManager(String path, Collection<AbstractModelState<E, S>> states) {
		super(path);
		this.states = states;
	}
	
	/* METHODS */
	@Override
	public AbstractModelState<E, S> getCurrentState() {
		return currentState;
	}
	
	@Override
	public void setCurrentState(AbstractModelState<E, S> currentState) {
		this.currentState = currentState;
	}
	
	@Override
	public Collection<AbstractModelState<E, S>> getStates() {
		return states;
	}
	
	@Override
	public void setStates(Collection<AbstractModelState<E, S>> states) {
		this.states = states;
	}
}

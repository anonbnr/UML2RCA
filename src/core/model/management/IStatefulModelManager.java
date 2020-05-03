package core.model.management;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * an IStatefulModelManager generic interface that defines the basic operations for
 * all stateful model managers.<br><br>
 * 
 * A stateful model manager is a manager that keeps track of the model's states throughout its lifecycle.
 * This interface provides access to the managed model's collection of states, specifically to its current state
 * It also provides an API to query and modify the model's collection of states 
 * (e.g. access by state description, test by state description, state creation, state addition, 
 * states display, etc.)<br><br>
 * 
 * It must be implemented by all concrete stateful model manager classes. 
 * @author Bachar Rima
 *
 * @param <E> the type of the model to manage.
 * @param <S> the type of the model state's description.
 */
public interface IStatefulModelManager<E, S> extends IModelManager<E> {
	
	/* METHODS */
	/**
	 * Returns the current state of this model manager's model
	 * @return the current state of this model manager's model
	 */
	AbstractModelState<E, S> getCurrentState();
	
	/**
	 * Sets the current state of this model manager's model
	 * @param currentState the value to set the current state of this model manager's model
	 */
	void setCurrentState(AbstractModelState<E, S> currentState);
	
	/**
	 * Returns the collection of states of this model manager's model
	 * @return the collection of states of this model manager's model
	 */
	Collection<AbstractModelState<E, S>> getStates();
	
	/**
	 * Sets the collection of states of this model manager's model
	 * @param states the value to set the collection of states of this model manager's model
	 */
	void setStates(Collection<AbstractModelState<E, S>> states);
	
	/**
	 * Checks whether the model managed by this model manager has a state described by the provided
	 * description in its collection of states
	 * @param description the state description to search for in the collection of states
	 * of this model manager's model
	 * @return true if the model managed by this model manager has a state described by
	 * the provided state description in its collection of states, false otherwise
	 */
	default boolean hasStateDescribedBy(S description) {
		return getStates()
				.stream()
				.map(state -> state.getDescription())
				.anyMatch(stateDescription -> stateDescription.equals(description));
	}
	
	/**
	 * Returns the state described by the provided state description of the model managed 
	 * by this model manager, among the collection of its states (if it exists), 
	 * otherwise throws NotAValidModelStateException
	 * @param description the state description to search for in the collection of states
	 * of this model manager's model
	 * @return the model state of this model manager's managed model, 
	 * described by the provided state description, if it exists
	 * @throws NotAValidModelStateException if this model manager's model has no state 
	 * described by the provided state description
	 */
	default AbstractModelState<E, S> getStateDescribedBy(S description)
			throws NotAValidModelStateException {
		
		if(!hasStateDescribedBy(description))
			throw new NotAValidModelStateException(description + " is not a valid state of this model");
		
		return getStates()
				.stream()
				.filter(state -> state.getDescription().equals(description))
				.collect(Collectors.toList())
				.iterator()
				.next();
	}
	
	/**
	 * Creates a model state instance from the provided model and model state description,
	 * as it is implemented by the provided model state class
	 * @param <T> a class implementing a model state
	 * @param model a model defining the model component of the created model state instance
	 * @param description a description defining the description component of the 
	 * created model state instance
	 * @param stateClass the class creating the model state from the provided 
	 * model and description components
	 * @return a model state instance from the provided model and model state description
	 * as it is implemented by the provided model state class
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> AbstractModelState<E, S> createState(
			E model, S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException {
		
		 AbstractModelState<E, S> newState = stateClass.newInstance();
		 newState.setModel(model);
		 newState.setDescription(description);
		 
		 return newState;
	}
	
	/**
	 * Creates a model state instance from the provided model and model state description,
	 * as it is implemented by the provided model state class, and adds it to the collection
	 * of states of this model manager's model
	 * @param <T> a class implementing a model state
	 * @param model a model defining the model component of the created model state instance to be added
	 * @param description a description defining the description component of the 
	 * created model state instance to added
	 * @param stateClass the class creating the model state to be added from the provided 
	 * model and description components
	 * @return true if the created model state has been added successfully to the collection of 
	 * states of this model manager's managed model, false otherwise
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> boolean addState(E model, 
			S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException {
		
		return getStates().add(createState(model, description, stateClass));
	}
	
	/**
	 * Updates the model component of the model state of this model manager's managed model,
	 * described by the provided model state description (if it exists), otherwise
	 * creates a model state instance from the provided model and model state description,
	 * as it is implemented by the provided model state class, and adds it to the collection
	 * of states of this model manager's model
	 * @param <T> a class implementing a model state
	 * @param model a model defining the model component of the model state instance to be updated
	 * or created and added to the collection of states of this model manager's managed model
	 * @param description a description defining the description component of the 
	 * model state instance to be updated or created and added to the collection of states 
	 * of this model manager's managed model
	 @param stateClass the class creating the model state to be added from the provided 
	 * model and description components
	 * @return true if the model state described by the provided model state description already exists
	 * and its model component has been updated successfully. If no model state described by the provided
	 * model state description exists, returns true if the created model state has been added successfully 
	 * to the collection of states of this model manager's managed model, false otherwise
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> boolean updateOrAddState(E model, S description, 
			Class<T> stateClass) throws InstantiationException, IllegalAccessException {
		
		if (hasStateDescribedBy(description)) {
			try {
				getStateDescribedBy(description).setModel(model);
			} catch (NotAValidModelStateException e) {
				e.printStackTrace();
			}
			return true;
		}
		
		else
			return addState(model, description, stateClass);
	}
	
	/**
	 * Updates or creates the model state instance defined by the provided model and state description 
	 * components, as it is implemented by the provided model state class, and sets it as this model 
	 * manager's model current state
	 * @param <T> a class implementing a model state
	 * @param model a model defining the model component of the model state instance to be updated/created 
	 * and set as this model manager's managed model's current state (and therefore as this model manager's 
	 * managed model)
	 * @param description a description defining the description component of the model state instance to 
	 * be updated/created and set as this model manager's managed model's current state 
	 * of this model manager's managed model
	 * @param stateClass the class creating the model state from the provided 
	 * model and description components
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> void saveState(E model, S description, Class<T> stateClass)
			throws InstantiationException, IllegalAccessException {
		
		updateOrAddState(model, description, stateClass);
		
		try {
			setCurrentState(getStateDescribedBy(description));
		} catch (NotAValidModelStateException e) {
			e.printStackTrace();
		}
		
		setModel(getCurrentState().getModel());
	}
	
	/**
	 * Saves the the model state instance defined by the provided model and state description components, 
	 * as it is implemented by the provided model state class, using the saveState() method,
	 * and exports the model at the provided path, while setting the provided path as the path 
	 * associated to the model manager's managed model
	 * @param <T> a class implementing a model state
	 * @param path the path at which to export the provided model
	 * @param model a model that defines the model component of the model state instance to be saved, and
	 * to be exported at the provided path
	 * @param description a description defining the description component of the model state instance 
	 * to be saved
	 * @param stateClass the class creating the model state to be saved from the provided 
	 * model and description components
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> void saveStateAndExport(String path, E model, S description, 
			Class<T> stateClass) throws InstantiationException, IllegalAccessException {
		
		saveState(model, description, stateClass);
		setPath(path);
		exportModel(model, path);
	}
	
	/**
	 * Sets the state described by the provided model state description as this model manager's model
	 * current state (if it exists)
	 * @param description the state description to search for in the collection of states
	 * of this model manager's model
	 * @throws NotAValidModelStateException if this model manager's model has no state 
	 * described by the provided state description
	 */
	default void loadStateDescribedBy(S description) throws NotAValidModelStateException {
		setCurrentState(getStateDescribedBy(description));
		setModel(getCurrentState().getModel());
	}
	
	/**
	 * Sets the initial state of this model manager's model as its current state
	 */
	default void loadInitialState() {
		setCurrentState(getStates().iterator().next());
		setModel(getCurrentState().getModel());
	}
	
	/**
	 * Imports the model located at the provided path, for it to be managed by this model manager,
	 * and saves it along with the provided description in the collection of states associated with this
	 * model manager's managed model, while setting the provided path as the latter's associated path
	 * @param <T> a class implementing a model state
	 * @param path the path from which to import the model to be managed by this model manager
	 * @param description a description defining the description component of the model state instance 
	 * to be associated with the imported model
	 * @param stateClass the class creating the model state from the provided 
	 * model and description components
	 * @throws InstantiationException if the provided class implementing the model state cannot be instantiated
	 * @throws IllegalAccessException if the components of the provided class implementing the model state 
	 * cannot be accessed from the scope using this method
	 */
	default <T extends AbstractModelState<E, S>> void importAndLoadState(String path, S description, 
			Class<T> stateClass) throws InstantiationException, IllegalAccessException {
		
		setPath(path);
		saveState(importModel(path), description, stateClass);
	}
	
	/**
	 * Displays the descriptions of each state in the collection of model states associated 
	 * with this model manager's model
	 */
	default void displayStates() {
		getStates()
		.stream()
		.map(state -> state.getDescription())
		.forEach(System.out::println);
	}
}

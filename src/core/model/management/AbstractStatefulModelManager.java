package core.model.management;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public abstract class AbstractStatefulModelManager<E, S> extends AbstractModelManager<E> {

	/* ATTRIBUTES */
	protected AbstractModelState<E, S> currentState;
	protected Collection<AbstractModelState<E, S>> states;
	
	/* CONSTRUCTORS */
	public AbstractStatefulModelManager(String path) {
		super(path);
		states = new LinkedList<>();		
	}
	
	public AbstractStatefulModelManager(String path, 
			Collection<AbstractModelState<E, S>> states) {
		
		super(path);
		this.states = states;
	}
	
	/* METHODS */
	public IModelState<E, S> getCurrentState() {
		return currentState;
	}
	
	public boolean hasStateDescribedBy(S description) {
		return states
				.stream()
				.map(state -> state.getDescription())
				.anyMatch(stateDescription -> stateDescription.equals(description));
	}
	
	public AbstractModelState<E, S> getStateDescribedBy(S description)
			throws NotAValidModelStateException {
		
		if(!hasStateDescribedBy(description))
			throw new NotAValidModelStateException(description + " is not a valid state of this model");
		
		return states
				.stream()
				.filter(state -> state.getDescription().equals(description))
				.collect(Collectors.toList())
				.iterator()
				.next();
	}
	
	protected <T extends AbstractModelState<E, S>> AbstractModelState<E, S> createState(
			E model, S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException {
		
		 AbstractModelState<E, S> newState = stateClass.newInstance();
		 newState.setModel(model);
		 newState.setDescription(description);
		 
		 return newState;
	}
	
	protected <T extends AbstractModelState<E, S>> boolean addState(E model, 
			S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException {
		
		return states.add(createState(model, description, stateClass));
	}
	
	protected <T extends AbstractModelState<E, S>> boolean updateOrAddState(
			E model, S description, Class<T> stateClass) 
					throws NotAValidModelStateException, InstantiationException, 
					IllegalAccessException {
		
		if (hasStateDescribedBy(description)) {
			getStateDescribedBy(description).setModel(model);
			return true;
		}
		
		else
			return addState(model, description, stateClass);
	}
	
	public <T extends AbstractModelState<E, S>> void saveState(E model, 
			S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException, 
					NotAValidModelStateException {
		
		updateOrAddState(model, description, stateClass);
		this.currentState = getStateDescribedBy(description);
		this.model = currentState.getModel();
	}
	
	public <T extends AbstractModelState<E, S>> void saveStateAndExport(
			String path, E model, S description, Class<T> stateClass)
					throws InstantiationException, IllegalAccessException, 
					NotAValidModelStateException {
		
		saveState(model, description, stateClass);
		this.path = path;
		exportModel(model, path);
	}
	
	public void loadStateDescribedBy(S description) 
			throws NotAValidModelStateException {
		
		this.currentState = getStateDescribedBy(description);
		this.model = currentState.getModel();
	}
	
	public void loadInitialState() {
		this.currentState = states.iterator().next();
		this.model = currentState.getModel();
	}
	
	public <T extends AbstractModelState<E, S>> void importAndLoadState(
			String path, S description, Class<T> stateClass) 
					throws InstantiationException, IllegalAccessException, 
					NotAValidModelStateException {
		
		this.path = path;
		saveState(importModel(path), description, stateClass);
	}
	
	public void displayStates() {
		states
		.stream()
		.map(state -> state.getDescription())
		.forEach(System.out::println);
	}
}

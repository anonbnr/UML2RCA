package core.model.management;

/**
 * an AbstractModelManager generic abstract class that is used to factor 
 * the common interface and state of all concrete model manager classes.<br><br>
 *  
 * It must be specialized by all concrete model manager classes.
 * 
 * @author Bachar Rima
 *
 * @param <E> the type of the model to manage.
 */
public abstract class AbstractModelManager<E> implements IModelManager<E> {

	/* ATTRIBUTES */
	/**
	 * The model managed by this model manager
	 */
	protected E model;
	
	/**
	 * The path associated to the model managed by this model manager
	 */
	protected String path;
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty model manager
	 */
	public AbstractModelManager() {}
	
	/**
	 * Creates a model manager to manage the model associated to a specific path. The importation
	 * details of the model associated to the path will be explicited in the constructors of the 
	 * concrete implementations of this class.
	 * @param path the path associated to the model to be managed by this model manager
	 */
	public AbstractModelManager(String path) {
		this.path = path;
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
	public String getPath() {
		return path;
	}
	
	@Override
	public void setPath(String path) {
		this.path = path;
	}
}
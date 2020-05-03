package core.model.management;

/**
 * an IModelManager generic interface that defines the basic operations for
 * all model managers.<br><br>
 * 
 * It provides the signatures of the model import/export operations, model getting operation, and
 * model path getting operation.<br><br>
 * 
 * It must be implemented by all concrete model manager classes.
 * 
 * @author Bachar Rima
 *
 * @param <E> the type of the model to manage.
 */
public interface IModelManager<E> {
	
	/* METHODS */
	/**
	 * Exports the model managed by this model manager to the specified path
	 * @param model the model managed by this model manager
	 * @param path the path at which the model managed by this model manager should be exported
	 * @return true if the model was exported successfully, false otherwise
	 */
	boolean exportModel(E model, String path);
	
	/**
	 * Imports a model from the specified path, to be managed by this model manager
	 * @param path the path from which the model, to be managed by this model manager, should be imported
	 * @return the imported model managed by this model manager
	 */
	E importModel(String path);
	
	/**
	 * Returns the model managed by this model manager
	 * @return the model managed by this model manager
	 */
	E getModel();
	
	/**
	 * Sets the model managed by this model manager
	 * @param model a value to set the model managed by this model manager
	 */
	void setModel(E model);
	
	/**
	 * Returns the path associated to the model managed by this model manager
	 * @return the path associated to the model managed by this model manager
	 */
	String getPath();
	
	/**
	 * Sets the path associated to the model managed by this model manager
	 * @param path a value to set the path associated to the model managed by this model manager
	 */
	void setPath(String path);
}

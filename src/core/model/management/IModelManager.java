package core.model.management;

/**
 * an IModelManager generic interface that defines the basic import/export operations for
 * all model managers.<br><br>
 * 
 * It must be implemented by all concrete model manager classes.
 * 
 * @author Bachar.RIMA
 *
 * @param <E> the type of the model to import/export 
 */
public interface IModelManager<E> {
	boolean exportModel(E modelElement, String stringURI);
	E importModel(String stringURI);
	E getModel();
	String getPath();
}

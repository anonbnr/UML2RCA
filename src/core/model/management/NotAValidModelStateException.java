package core.model.management;

/**
 * Thrown when a model state is not valid. One possible case of such an event is upon searching for 
 * a model state description identifying a model state in a collection of model states 
 * maintained by a model manager and not finding any.
 * @author Bachar Rima
 *
 */
public class NotAValidModelStateException extends Exception {
	public NotAValidModelStateException() {super();}
	public NotAValidModelStateException(String message) {super(message);}
}

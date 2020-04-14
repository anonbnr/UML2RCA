package core.transformation;

/**
 * an AbstractTransformation abstract class that is used to factor the common interface and state
 * of all concrete atomic metamodeling transformation classes.<br/><br/>
 * 
 * It must be specialized by all concrete metamodeling transformation classes.
 * @author Bachar Rima
 * @see ITransformation
 *
 * @param <S> The type of the source element to transform.
 * @param <T> The type of the target element to obtain.
 */
public abstract class AbstractTransformation<S, T> implements ITransformation<S, T> {
	
	/* ATTRIBUTES */
	protected S source;
	protected T target;
	
	/* CONSTRUCTORS */
	public AbstractTransformation() {}
	
	/**
	 * creates an instance of AbstractTransformation from a source element
	 * and applies the transformation afterwards to directly get the target.
	 * @param source a source element to transform.
	 */
	public AbstractTransformation(S source) {
		this.setSource(source);
		this.setTarget(this.transform(source));
	}
	
	/* METHODS */
	public S getSource() {return this.source;}
	public void setSource(S source) {this.source = source;}
	
	public T getTarget() {return this.target;}
	public void setTarget(T target) {this.target = target;}
}

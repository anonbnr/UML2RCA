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
	/**
	 * The transformation's source
	 */
	protected S source;
	
	/**
	 * The transformation's target
	 */
	protected T target;
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty transformation
	 */
	public AbstractTransformation() {}
	
	/**
	 * Creates an transformation from a source element
	 * and applies the transformation afterwards to directly get the target.
	 * @param source a source element to transform.
	 */
	public AbstractTransformation(S source) {
		this.setSource(source);
		this.setTarget(this.transform(source));
	}
	
	/* METHODS */
	@Override
	public S getSource() {
		return this.source;
	}
	
	@Override
	public void setSource(S source) {
		this.source = source;
	}
	
	@Override
	public T getTarget() {
		return this.target;
	}
	
	@Override
	public void setTarget(T target) {
		this.target = target;
	}
}

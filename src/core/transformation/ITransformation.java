package core.transformation;

/**
 * an ITransformation generic interface defining an atomic transformation operation
 * of a source element into a target element.<br/><br/>
 * 
 * This interface provides a generic transformation operation between model elements
 * conforming to two random metamodels, or possibly the same metamodel.<br/><br/>
 * 
 * It must be implemented by all concrete atomic metamodeling transformation classes.
 * 
 * @author Bachar RIMA
 *
 * @param <S> The type of the source element to transform.
 * @param <T> The type of the target element to obtain.
 */
public interface ITransformation<S,T> {
	
	/* METHODS */
	/**
	 * Returns this transformation's source
	 * @return this transformation's source
	 */
	S getSource();
	
	/**
	 * Sets this transformation's source
	 * @param source the value to set this transformation's source
	 */
	void setSource(S source);
	
	/**
	 * Returns this transformation's target
	 * @return this transformation's target
	 */
	T getTarget();
	
	/**
	 * Sets this transformation's target
	 * @param target the value to set this transformation's target
	 */
	void setTarget(T target);
	
	/**
	 * Executes some pre-transformation processing on the environment 
	 * and state of the transformation class implementing this interface
	 * @param source the source model element to transform.
	 */
	void preTransform(S source);
	
	/**
	 * Transforms a source model element conforming to a random metamodel into
	 * a target model element conforming to another random metamodel (possibly the same)
	 * in an atomic manner.
	 * @param source the source model element to transform.
	 * @return the target model element.
	 */
	T transform(S source);
	
	/**
	 * Executes some post-transformation processing on the environment 
	 * and state of the transformation class implementing this interface
	 * @param source the source model element to transform.
	 */
	void postTransform(S source);
	
	/**
	 * Applies the pre-transformation, transformation, and post-transformation methods as they
	 * are defined by the transformation class implementing this interface
	 * @param source the source model element to transform.
	 */
	default void apply(S source) {
		preTransform(source);
		setTarget(transform(source));
		postTransform(source);
	}
}

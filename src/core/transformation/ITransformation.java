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
	/**
	 * An atomic metamodeling transformation operation of a
	 * source model element conforming to a random metamodel into
	 * a target model element conforming to another random metamodel.
	 * @param source the source model element to transform.
	 * @return the target model element.
	 */
	T transform(S source);
}

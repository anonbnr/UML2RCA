package core.transformation;

import java.util.Collection;

/**
 * an ITransformationStrategy interface of metamodeling transformation strategies.<br/><br/>
 * 
 * This interface provides a method that returns the collection of atomic
 * metamodeling transformation operations composing the strategy. Every transformation
 * strategy is composed of a collection of atomic metamodeling transformations. The 
 * data structure specializing the collection is to be explicitly chosen by the client 
 * according to his needs. Some strategies, for instance, represent classical 
 * sequential transformation algorithms, while others might depict a more complex
 * transformation schema requiring a hash map for example.<br/><br/>
 *  
 * It must be implemented by all concrete metamodeling transformation strategy classes.
 * @author Bachar Rima
 * @see ITransformation
 */
public interface ITransformationStrategy {
	
	/**
	 * an operation that returns the collection of atomic metamodeling
	 * transformation operations composing the strategy
	 * @return the collection of transformation operations composing the strategy
	 */
	Collection<ITransformation<?,?>> transformations();
}

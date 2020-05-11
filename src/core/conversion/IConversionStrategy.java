package core.conversion;

import core.transformation.ITransformationStrategy;

/**
 * an IConversionStrategy interface of metamodeling conversion strategies.<br><br>
 * 
 * This interface provides a method that returns the collection of atomic
 * metamodeling conversion operations composing the strategy. Every conversion
 * strategy is composed of a collection of atomic metamodeling conversions. The 
 * data structure specializing the collection is to be explicitly chosen by the client 
 * according to his needs.<br><br>
 *  
 * It must be implemented by all concrete metamodeling conversion strategy classes.
 * @author Bachar Rima
 * @see IConversion
 * @see ITransformationStrategy
 */
public interface IConversionStrategy extends ITransformationStrategy {
	
}

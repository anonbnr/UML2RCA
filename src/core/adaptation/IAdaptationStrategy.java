package core.adaptation;

import core.transformation.ITransformationStrategy;

/**
 * an IAdaptationStrategy interface of metamodeling adaptation strategies.<br><br>
 * 
 * This interface provides a method that returns the collection of atomic
 * metamodeling adaptation operations composing the strategy. Every adaptation
 * strategy is composed of a collection of atomic metamodeling adaptations. The 
 * data structure specializing the collection is to be explicitly chosen by the client 
 * according to his needs.<br><br>
 *  
 * It must be implemented by all concrete metamodeling adaptation strategy classes.
 * 
 * @author Bachar Rima
 * @see IAdaptation
 * @see ITransformationStrategy
 */
public interface IAdaptationStrategy extends ITransformationStrategy {

}

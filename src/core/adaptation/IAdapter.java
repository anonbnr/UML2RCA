package core.adaptation;

import core.transformation.ITransformer;

/**
 * an IAdapter interface of a metamodeling adapter.<br><br>
 * 
 * This interface provides a method that returns the metamodeling adaptation
 * strategy used by the adapter.<br><br>
 *  
 * It must be implemented by all concrete metamodeling adapter classes.
 * 
 * @author Bachar Rima
 * @see IAdaptationStrategy
 * @see ITransformer
 */
public interface IAdapter extends ITransformer {

}

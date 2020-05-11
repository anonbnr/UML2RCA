package core.conversion;

import core.transformation.ITransformer;

/**
 * an IConverter interface of a metamodeling converter.<br><br>
 * 
 * This interface provides a method that returns the metamodeling conversion
 * strategy used by the adapter.<br><br>
 *  
 * It must be implemented by all concrete metamodeling converter classes.
 * @author Bachar Rima
 * @see IConversionStrategy
 * @see ITransformer
 */
public interface IConverter extends ITransformer {

}

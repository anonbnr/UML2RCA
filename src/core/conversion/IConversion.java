package core.conversion;

import core.transformation.ITransformation;

/**
 * an IConversion generic interface defining an atomic conversion operation
 * of a source element into a target element.<br/><br/>
 * 
 * This interface provides a generic conversion operation between model elements
 * conforming to different metamodels (i.e. inter-metamodel transformation).<br/><br/>
 * 
 * It must be implemented by all concrete atomic metamodeling conversion classes.
 * 
 * @author Bachar RIMA
 * @see ITransformation
 *
 * @param <S> The type of the source element to convert.
 * @param <T> The type of the target element to obtain.
 */
public interface IConversion<S, T> extends ITransformation<S, T> {

}

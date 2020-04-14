package core.adaptation;

import core.transformation.ITransformation;

/**
 * an IAdaptation generic interface defining an atomic adaptation operation
 * of a source element into a target element.<br/><br/>
 * 
 * This interface provides a generic adaptation operation between model elements
 * conforming to the same metamodel (i.e. intra-metamodel transformation).<br/><br/>
 * 
 * It must be implemented by all concrete atomic metamodeling adaptation classes.
 * 
 * @author Bachar RIMA
 * @see ITransformation
 *
 * @param <S> The type of the source element to adapt.
 * @param <T> The type of the target element to obtain.
 */
public interface IAdaptation<S, T> extends ITransformation<S, T> {

}

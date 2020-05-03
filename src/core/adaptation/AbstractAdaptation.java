package core.adaptation;

import core.transformation.AbstractTransformation;

/**
 * an AbstractAdaptation abstract class that is used to factor the common interface and state
 * of all concrete atomic metamodeling adaptation classes.<br><br>
 * 
 * It must be specialized by all concrete metamodeling adaptation classes.
 * 
 * @author Bachar Rima
 * @see AbstractTransformation
 * @see IAdaptation
 *
 * @param <S> The type of the source element to adapt.
 * @param <T> The type of the target element to obtain.
 */
public abstract class AbstractAdaptation<S, T> extends AbstractTransformation<S, T> implements IAdaptation<S, T> {

	/* CONSTRUCTORS */
	/**
	 * Creates an empty adaptation
	 */
	public AbstractAdaptation() {super();}
	
	/**
	 * Creates an adaptation from a source element
	 * and applies the adaptation afterwards to directly get the target.
	 * @param source a source element to adapt.
	 */
	public AbstractAdaptation(S source) {super(source);}
}

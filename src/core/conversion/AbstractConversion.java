package core.conversion;

import core.transformation.AbstractTransformation;

/**
 * an AbstractConversion abstract class that is used to factor the common interface and state
 * of all concrete atomic metamodeling conversion classes.<br><br>
 * 
 * It must be specialized by all concrete metamodeling conversion classes.
 * 
 * @author Bachar Rima
 * @see AbstractTransformation
 * @see IConversion
 *
 * @param <S> The type of the source element to convert.
 * @param <T> The type of the target element to obtain.
 */
public abstract class AbstractConversion<S, T> extends AbstractTransformation<S, T> implements IConversion<S, T> {
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty conversion
	 */
	public AbstractConversion() {super();}
	
	/**
	 * Creates an conversion from a source element
	 * and applies the conversion afterwards to directly get the target.
	 * @param source a source element to convert.
	 */
	public AbstractConversion(S source) {super(source);}
}

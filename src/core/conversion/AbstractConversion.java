package core.conversion;

import core.transformation.AbstractTransformation;

/**
 * an AbstractConversion abstract class that is used to factor the common interface and state
 * of all concrete atomic metamodeling conversion classes.<br/><br/>
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
	public AbstractConversion() {super();}
	public AbstractConversion(S source) {super(source);}
}

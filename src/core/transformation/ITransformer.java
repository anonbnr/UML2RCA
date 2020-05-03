package core.transformation;

/**
 * an ITransformer interface of a metamodeling transformer.<br/><br/>
 * 
 * This interface provides a method that returns the metamodeling transformation
 * strategy used by the transformer.<br/><br/>
 *  
 * It must be implemented by all concrete metamodeling transformer classes.
 * @author Bachar Rima
 * @see ITransformationStrategy
 */
public interface ITransformer {
	
	/* METHODS */
	/**
	 * Returns this transformer's transformation strategy
	 * @return this transformer's transformation strategy
	 */
	ITransformationStrategy strategy();
}

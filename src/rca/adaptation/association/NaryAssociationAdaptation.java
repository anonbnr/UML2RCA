package rca.adaptation.association;

import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import exceptions.NotAnNAryAssociationException;
import rca.utility.Associations;

/**
 * an NaryAssociationAdaptation generic abstract class that is used to factor common state for all 
 * concrete UML n-ary association adaptation classes.<br/><br/>
 * 
 * It must be specialized by all concrete UML n-ary association adaptation classes.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public abstract class NaryAssociationAdaptation<T> extends AbstractAdaptation<Association, T> {
	
	/* CONSTRUCTOR */
	public NaryAssociationAdaptation(Association source)
			throws NotAnNAryAssociationException {
		
		if(!Associations.isNary(source))
			throw new NotAnNAryAssociationException(source.getName() + 
					" is not an n-ary association");
		
		this.setSource(source);
		this.setTarget(this.transform(source));
		this.postTransformationClean();
	}
	
	/* METHODS */
	protected abstract void postTransformationClean();
}

package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

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
public abstract class AbstractNaryAssociationAdaptation<T> extends AbstractAdaptation<Association, T> {
	
	/* ATTRIBUTES */
	protected List<Association> associations;
	
	/* CONSTRUCTOR */
	public AbstractNaryAssociationAdaptation(Association source)
			throws NotAnNaryAssociationException {
		
		if(!Associations.isNary(source))
			throw new NotAnNaryAssociationException(source.getName() + 
					" is not an n-ary association");
		
		this.setSource(source);
		associations = new ArrayList<>();
		this.setTarget(this.transform(source));
		this.postTransformationClean();
	}
	
	/* METHODS */
	public List<Association> getAssociations() {
		return associations;
	}
	
	protected abstract void postTransformationClean();
}

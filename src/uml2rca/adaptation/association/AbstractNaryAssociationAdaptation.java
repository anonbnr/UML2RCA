package uml2rca.adaptation.association;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Association;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotAnNaryAssociationException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an NaryAssociationAdaptation generic abstract class that is used to factor common state for all 
 * concrete UML n-ary association adaptation classes.<br><br>
 * 
 * It must be specialized by all concrete UML n-ary association adaptation classes.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Association
 */
public abstract class AbstractNaryAssociationAdaptation<T> extends AbstractAdaptation<Association, T> {
	
	/* ATTRIBUTES */
	/**
	 * The list of associations generated by this adaptation. Its semantics
	 * are defined by the concrete n-ary adaptation class specializing this class
	 * (e.g. for an NaryAssociationMaterializationAdaptation, this attribute will refer
	 * to the associations connecting the materialized target class, replacing the n-ary association
	 * to adapt, to the original members of the n-ary association to adapt, 
	 * whereas for an NaryAssociationToBinaryAssociationsAdaptation, this
	 * attribute will refer to the target binary associations replacing the n-ary association 
	 * to adapt)
	 */
	protected List<Association> associations;
	
	/* CONSTRUCTORS */
	/**
	 * Creates an empty n-ary association adaptation
	 */
	public AbstractNaryAssociationAdaptation() {}
	
	/**
	 * Creates an n-ary association adaptation having source as its source n-ary association to adapt, 
	 * then applies the adaptation to obtain the target entity, and cleans the post-adaptation
	 * residues.
	 * @param source the source n-ary association to adapt
	 * @throws NotAnNaryAssociationException if the provided source entity is not an n-ary association
	 */
	public AbstractNaryAssociationAdaptation(Association source)
			throws NotAnNaryAssociationException {
		
		if(!Associations.isNary(source))
			throw new NotAnNaryAssociationException(source.getName() + 
					" is not an n-ary association");
		
		apply(source);
	}
	
	/* METHODS */
	/**
	 * Returns the list of associations generated by this adaptation
	 * @return the list of associations generated by this adaptation
	 */
	public List<Association> getAssociations() {
		return associations;
	}
	
	/**
	 * Sets the provided entity as the source of this adaptation
	 * and initializes the list of associations generated by this adaptation
	 */
	@Override
	public void preTransform(Association source) {
		super.preTransform(source);
		associations = new ArrayList<>();
	}
}

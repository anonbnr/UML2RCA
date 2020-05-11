package uml2rca.adaptation.association;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;

import uml2rca.exceptions.NotAnAssociationClassException;
import uml2rca.java.uml2.uml.extensions.utility.Associations;

/**
 * an AssociationClassToConcreteClassAdaptation concrete class that is used to adapt a UML
 * association class into an equivalent concrete class.<br><br>
 * 
 * The adaptation consists of:
 * <ol>
 * <li>creating an equivalent target concrete class, having the same owning package, 
 * the same name, and the same owned attributes of the source association class.</li>
 * <li>creating n binary associations between the created target concrete class and the n types participating
 * in the source association class.</li>
 * </ol>
 * 
 * @author Bachar Rima
 * @see NaryAssociationMaterializationAdaptation
 * @see AssociationClass
 * @see Class
 */
public class AssociationClassToConcreteClassAdaptation extends NaryAssociationMaterializationAdaptation {

	/* CONSTRUCTOR */
	/**
	 * Creates an association class to concrete class adaptation having source as its source
	 * association class to adapt, then applies the adaptation to obtain the target class
	 * and the n binary associations, and cleans the post-adaptation residues.
	 * @param source the source association class to adapt
	 * @throws NotAnAssociationClassException if the provided source entity is not an association class
	 */
	public AssociationClassToConcreteClassAdaptation(Association source) 
			throws NotAnAssociationClassException {
		
		if(!Associations.isAssociationClass(source))
			throw new NotAnAssociationClassException(source.getName() + 
					" is not an association class");
		
		apply(source);
	}
	
	/* METHODS */
	/**
	 * Creates an equivalent target concrete class, having the same owning package 
	 * and the same name as the source association class to adapt, 
	 * then creates n binary associations between the target class and the n types 
	 * participating in source, and finally creates for the target class 
	 * the same attributes owned by the source association class to adapt.
	 */
	@Override
	public Class transform(Association source) {
		Class target = super.transform(source);
		initTargetClassAttributes((AssociationClass) source, target);
		
		return target;
	}
	
	/**
	 * Creates the same attributes owned by source for target
	 * @param source the source association class to adapt
	 * @param target the target concrete class that'll have the same owned attributes as source
	 */
	protected void initTargetClassAttributes(AssociationClass source, Class target) {
		source.getOwnedAttributes()
		.stream()
		.forEach(ownedAttribute -> 
			target.createOwnedAttribute(ownedAttribute.getName(), ownedAttribute.getType()));
	}
}

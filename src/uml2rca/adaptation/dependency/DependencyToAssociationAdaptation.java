package uml2rca.adaptation.dependency;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import uml2rca.exceptions.NotATypeException;
import uml2rca.java.extensions.utility.Strings;
import uml2rca.java.uml2.uml.extensions.utility.Dependencies;

/**
 * a DependencyToAssociationAdaptation concrete class that is used to adapt a UML
 * dependency into an equivalent list of general associations.<br><br>
 * 
 * The adaptation consists of creating a list of equivalent target general associations
 * between each client and each supplier of the source dependency, 
 * having each the same owning package as the source dependency.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Dependency
 * @see Association
 */
public class DependencyToAssociationAdaptation extends AbstractAdaptation<Dependency, List<Association>> {
	
	/* CONSTRUCTOR */
	/**
	 * Creates a dependency to association adaptation having source as its source dependency
	 * to adapt, then applies the adaptation to obtain the list of equivalent target general associations 
	 * between each client and each supplier of the source dependency,
	 * and cleans the post-adaptation residues.
	 * @param source the source dependency to adapt.
	 * @throws NotATypeException if the provided source dependency has a client or a supplier that is not a typed element
	 */
	public DependencyToAssociationAdaptation(Dependency source)
			throws NotATypeException {
		
		Dependencies.validateDependencyEnds(source, source.getClients());
		Dependencies.validateDependencyEnds(source, source.getSuppliers());
		apply(source);
	}

	/* METHODS */
	/**
	 * Creates a list of equivalent target unidirectional general associations
	 * between each client and each supplier of the source dependency to adapt, 
	 * having each the same owning package as it.
	 */
	@Override
	public List<Association> transform(Dependency source) {
		List<Association> associations = new ArrayList<>();
		Association association = null;
		
		for (NamedElement client: source.getClients()) {
			for (NamedElement supplier: source.getSuppliers()) {
				
				association = initTargetAssociation(source, client, supplier);
				initTargetAssociationMemberEnd(association, client, false);
				initTargetAssociationMemberEnd(association, supplier, true);
				associations.add(association);
			}
		}

		return associations;
	}
	
	/**
	 * Creates a general binary association from source between one of its clients and
	 * one of its suppliers, having the same owning package as it
	 * @param source the source dependency to adapt
	 * @param client one of the clients of source
	 * @param supplier one of the suppliers of source
	 * @return a general binary association from source between one of its clients and
	 * one of its suppliers, having the same owning package as it
	 */
	private Association initTargetAssociation(Dependency source, NamedElement client, NamedElement supplier) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setPackage(source.getNearestPackage());
		association.setName(
				Strings.decapitalize(client.getName()) + 
				"-" + source.getName() + "-" +
				Strings.decapitalize(supplier.getName()));
		return association;
	}
	
	/**
	 * Creates a member end for the provided general association, from the original dependency member,
	 * whose navigablity is defined by isNavigable
	 * @param association the general association for which this method creates a member end
	 * @param dependencyMember the original dependency member from which the member end of the
	 * general association will be created
	 * @param isNavigable a value determining whether the created general association member end is navigable or not
	 */
	private void initTargetAssociationMemberEnd(Association association, NamedElement dependencyMember, boolean isNavigable) {
		Property associationMemberEnd = UMLFactory.eINSTANCE.createProperty();
		associationMemberEnd.setOwningAssociation(association);
		associationMemberEnd.setIsNavigable(isNavigable);
		associationMemberEnd.setAggregation(AggregationKind.NONE_LITERAL);
		associationMemberEnd.setName(Strings.decapitalize(dependencyMember.getName()));
		associationMemberEnd.setLower(1);
		associationMemberEnd.setUpper(LiteralUnlimitedNatural.UNLIMITED);
		associationMemberEnd.setType((Type) dependencyMember);
	}
	
	/**
	 * Removes the source dependency from the model once its equivalent target general 
	 * associations have been created and added to the model to replace it.
	 */
	@Override
	public void postTransform(Dependency source) {
		source.destroy();
	}
}
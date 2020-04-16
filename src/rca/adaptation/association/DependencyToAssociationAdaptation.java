package rca.adaptation.association;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import core.adaptation.AbstractAdaptation;
import exceptions.NotATypeException;
import utility.Strings;

/**
 * a DependencyToAssociationAdaptation concrete class that is used to adapt a UML
 * dependency into an equivalent list of general associations.<br/><br/>
 * 
 * The adaptation consists of creating a list of equivalent target general associations
 * having each the same owning package and the same name as the source dependency.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Dependency
 * @see Association
 */
public class DependencyToAssociationAdaptation extends AbstractAdaptation<Dependency, EList<Association>> {
	
	/* CONSTRUCTOR */
	/**
	 * creates a DependencyToAssociationAdaptation instance from a source dependency.
	 * @param source a dependency to adapt.
	 * @throws NotATypeException if the source has a client or a supplier that is not a typed element
	 */
	public DependencyToAssociationAdaptation(Dependency source)
			throws NotATypeException {
		
		validateDependencyEnds(source, source.getClients());
		validateDependencyEnds(source, source.getSuppliers());
		
		this.setSource(source);
		this.setTarget(this.transform(source));
		postTransformationClean();
	}

	/* METHODS */
	// implementation of the IAdaptation interface
	@Override
	public EList<Association> transform(Dependency source) {
		
		EList<Association> associations = new BasicEList<>();
		Association association = null;
		
		for (NamedElement client: source.getClients()) {
			for (NamedElement supplier: source.getSuppliers()) {
				
				association = initAssociation(source, client, supplier);
				initDependencyEnd(association, client, false);
				initDependencyEnd(association, supplier, true);
				associations.add(association);
			}
		}

		return associations;
	}
	
	private void validateDependencyEnds(Dependency source, List<NamedElement> ends) throws NotATypeException {
		if (!(ends
		.stream()
		.allMatch(end -> (end instanceof Type))))
			throw new NotATypeException(source.getName() + 
					" is a dependency having a non-type client/supplier");
	}
	
	private Association initAssociation(Dependency source, NamedElement client, NamedElement supplier) {
		Association association = UMLFactory.eINSTANCE.createAssociation();
		association.setPackage(source.getNearestPackage());
		association.setName(
				Strings.decapitalize(client.getName()) + 
				"-" + source.getName() + "-" +
				Strings.decapitalize(supplier.getName()));
		return association;
	}
	
	private void initDependencyEnd(Association association, NamedElement dependencyEnd, boolean isNavigable) {
		Property newDependencyEnd = UMLFactory.eINSTANCE.createProperty();
		newDependencyEnd.setOwningAssociation(association);
		newDependencyEnd.setIsNavigable(isNavigable);
		newDependencyEnd.setAggregation(AggregationKind.NONE_LITERAL);
		newDependencyEnd.setName(Strings.decapitalize(dependencyEnd.getName()));
		newDependencyEnd.setLower(1);
		newDependencyEnd.setUpper(LiteralUnlimitedNatural.UNLIMITED);
		newDependencyEnd.setType((Type) dependencyEnd);
	}
	
	private void postTransformationClean() {
		source.destroy();
	}
}

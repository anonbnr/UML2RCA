package rca.conversion;

import org.eclipse.uml2.uml.Association;

import core.conversion.AbstractConversion;
import rca.RCAFactory;
import rca.RContext;

/**
 * an AssociationToRContextsConversion concrete class that is used to convert a R-UML-conforming
 * unidirectional binary association, having no owned attributes, between concrete R-UML conforming
 * classes, into an equivalent RCA relational context.<br/><br/>
 * 
 * The conversion consists of creating an equivalent target relational context 
 * having a name chosen by an expert or the same name as the source association in case no name
 * was provided.
 * 
 * @author Bachar Rima
 * @see AbstractConversion
 * @see Association
 * @see RContext
 */
public class AssociationToRContextConversion extends AbstractConversion<Association, RContext> {
	
	/* ATTRIBUTES */
	private String rContextName; //name that could be chosen by the expert
	
	/* CONSTRUCTORS */
	public AssociationToRContextConversion(Association source) {
		this.setSource(source);
		this.setContextName("");
		this.setTarget(this.transform(source));
	}
	
	public AssociationToRContextConversion(Association source, String rContextName) {
		this.setSource(source);
		this.setContextName(rContextName);
		this.setTarget(this.transform(source));
	}
	
	/* METHODS */
	public String getContextName() {return this.rContextName;}
	public void setContextName(String rContextName) {this.rContextName = rContextName;}
	
	// implementation of the IConversion interface
	@Override
	public RContext transform(Association source) {
		RContext context = RCAFactory.eINSTANCE.createRContext();
		nameRContext(context);
		
		return context;
	}
	
	/**
	 * Sets the name of an RCA target relational context.<br/><br/> 
	 * 
	 * If no name is provided by the expert, the source association's name 
	 * will be used to name the target relational context.
	 * @param context the RCA relational context to name
	 */
	private void nameRContext(RContext context) {
		if(this.rContextName.isEmpty())
			context.setName(this.source.getName());
		else
			context.setName(this.rContextName);
	}
}

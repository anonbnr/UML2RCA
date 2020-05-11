package uml2rca.conversion;

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
		super(source);
	}
	
	public AssociationToRContextConversion(Association source, String rContextName) {
		apply(source, rContextName);
	}
	
	/* METHODS */
	public String getContextName() {return this.rContextName;}
	public void setContextName(String rContextName) {this.rContextName = rContextName;}
	
	@Override
	public void preTransform(Association source) {
		super.preTransform(source);
		setContextName("");
	}
	
	public void preTransform(Association source, String rContextName) {
		super.preTransform(source);
		setContextName(rContextName);
	}
	
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
	
	@Override
	public void postTransform(Association source) {
		
	}
	
	public void apply(Association source, String rContextName) {
		preTransform(source, rContextName);
		setTarget(transform(source));
		postTransform(source);
	}
}

package uml2rca.adaptation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Relationship;

import core.adaptation.AbstractAdaptation;

/**
 * an AbstractRelationshipWithAbstractMembersAdaptation abstract generic class that is used to factor 
 * the common interface and state of all concrete relationships with abstract class members 
 * (e.g. associations, association classes, dependencies, etc.)<br><br>
 * 
 * The adaptation consists of creating a list of target relationships having the 
 * same owning package as the source relationship to adapt, such that the members of each 
 * target relationship consist of the original non abstract members of the source relationship
 * and a non abstract child of the original abstract member of the source relationship.
 * 
 * @author Bachar Rima
 * @see AbstractAdaptation
 * @see Relationship
 */
public abstract class AbstractRelationshipWithAbstractMembersAdaptation<E extends Relationship> extends AbstractAdaptation<E, List<E>> {

	/* ATTRIBUTES */
	/**
	 * The map associating each abstract member of the source relationship
	 * to adapt to the set of its non abstract subclasses
	 */
	protected Map<Class, Set<Class>> abstractMembersDictionary;
	
	/**
	 * The queue used to contain the generated intermediary relationships,
	 * having abstract members, starting from the source relationship to adapt,
	 * and that'll end up containing the target relationships with no abstract members
	 * to be dequeued and returned by this adaptation
	 */
	protected Queue<E> newRelationshipsQueue;
	
	/**
	 * The list of intermediary relationships to clean, having abstract members,
	 * which are generated from the source relationship to adapt 
	 * while creating the target relationships having no abstract members
	 */
	protected List<E> relationshipsToClean;
	
	/* CONSTRUCTOR */
	/**
	 * Creates an empty relationship with abstract members adaptation
	 */
	public AbstractRelationshipWithAbstractMembersAdaptation() {}
	
	/* METHODS */
	/**
	 * Sets the provided entity as the source of this adaptation
	 * and initializes the map of abstract members as an empty hash table, 
	 * the queue of intermediary relationships as an empty linked list,
	 * and the list of intermediary relationships to clean as an empty array list.
	 */
	@Override
	public void preTransform(E source) {
		super.preTransform(source);
		abstractMembersDictionary = new Hashtable<>();
		newRelationshipsQueue = new LinkedList<>();
		relationshipsToClean = new ArrayList<>();
	}
	
	/**
	 * Initializes the list of target relationships to be returned by this method 
	 * using the {@link #createNewRelationshipEmptyList()} method of this class, 
	 * and the content of the abstract members dictionary using
	 * the {@link #initSourceRelationshipAbstractMembersDictionary()} method of this class, 
	 * then adds the source relationship with abstract members to the queue 
	 * of intermediary relationships with abstract members.<br> 
	 * While the queue is not empty, it peeks at its head relationship <code>R</code>,
	 * and checks if <code>R</code> has any abstract member. If <code>R</code> 
	 * does have an abstract member <code>M</code>, then 
	 * for each non abstract subclass <code>S</code> of <code>M</code>, 
	 * it generates an intermediary relationship <code>I</code>
	 * that's identical to <code>R</code>, but that replaces <code>M</code> with 
	 * <code>S</code>, using the {@link #cloneAndAdaptIntermediaryRelationship(E, Class, Class)}
	 * method of this class, and enqueues <code>I</code> into the queue. It then
	 * adds <code>R</code> to the list of intermediary relationships to clean.
	 * If, on the other hand, <code>R</code> doesn't have any abstract member, 
	 * then <code>R</code> is one of the target relationships to be returned by 
	 * this adaptation, and that'll be added to the list of relationships returned by this method. 
	 * Finally, the method dequeues <code>R</code>.<br> 
	 * Moreover, once the queue is empty, each target relationship will be named appropriately
	 * and owned by the same owning package of the source relationship with abstract members
	 * to adapt, using the {@link #initTargetRelationshipsNamesAndOwnership(List)} method of this class.
	 */
	@Override
	public List<E> transform(E source) {
		List<E> newOwnedRelationships = createNewRelationshipEmptyList();
		initSourceRelationshipAbstractMembersDictionary();
		
		newRelationshipsQueue.add(source);
		
		while(!newRelationshipsQueue.isEmpty()) {
			final E currentRelationship = newRelationshipsQueue.element();
			
			if (hasAnAbstractMember(currentRelationship)) {
				Class abstractMember = getFirstAbstractMember(currentRelationship);
				
				abstractMembersDictionary.get(abstractMember)
				.stream()
				.forEach(subClass -> {
					E clonedRelationship = cloneAndAdaptIntermediaryRelationship(
							currentRelationship, abstractMember, subClass);
					newRelationshipsQueue.add(clonedRelationship);
				});
				
				relationshipsToClean.add(currentRelationship);
			}
			
			else
				newOwnedRelationships.add(currentRelationship);
			
			newRelationshipsQueue.remove();
		}
		
		initTargetRelationshipsNamesAndOwnership(newOwnedRelationships);
		
		return newOwnedRelationships;
	}

	/**
	 * Creates a new empty list of relationships
	 * @return a new empty list of relationships
	 */
	protected abstract List<E> createNewRelationshipEmptyList();
	
	/**
	 * Initializes the the content of the abstract members dictionary
	 * of the source relationship with abstract members to adapt, by
	 * mapping each of its abstract members to the set of its non abstract
	 * subclasses.
	 */
	protected abstract void initSourceRelationshipAbstractMembersDictionary();
	
	/**
	 * Checks whether the provided relationship has an abstract member.
	 * @param relationship the relationship to examine
	 * @return true if the provided relationship has an abstract member, false otherwise
	 */
	protected abstract boolean hasAnAbstractMember(E relationship);
	
	/**
	 * Returns the first abstract member of the provided relationship.
	 * @param relationship the relationship to examine
	 * @return the first abstract member of the provided relationship, or an empty Optional object if none can be found
	 * @see Optional
	 */
	protected abstract Class getFirstAbstractMember(E relationship);
	
	/**
	 * Clones the provided enqueued intermediary relationship, using the
	 * {@link #cloneIntermediaryRelationship(E)} method of this class,
	 * and adapts its first abstract member by replacing it with one of its non abstract subclasses,
	 * using the {@link #adaptIntermediaryRelationship(Relationship, Class, Class)}.
	 * @param intermediaryRelationship the intermediary relationship to clone 
	 * and whose first abstract member end will be adapted by this method
	 * @param abstractMember the first abstract member of the provided intermediary relationship whose
	 * corresponding member end will be adapted by this method
	 * @param subClass one of the non abstract subclasses of the provided abstract member 
	 * of the provided intermediary relationship, that will replace the abstract member in the
	 * relationship returned by this method
	 * @return the cloned relationship obtained from the provided enqueued intermediary relationship
	 * and whose first abstract member is adapted by replacing it with the provided non abstract
	 * subclass
	 */
	protected E cloneAndAdaptIntermediaryRelationship(E intermediaryRelationship, Class abstractMember, Class subClass) {
		E newOwnedRelationship = cloneIntermediaryRelationship(intermediaryRelationship);
		newOwnedRelationship = adaptIntermediaryRelationship(newOwnedRelationship, abstractMember, subClass);
		return newOwnedRelationship;
	}

	/**
	 * Clones the provided enqueued intermediary relationship.
	 * @param intermediaryRelationship the intermediary relationship to clone
	 * @return the cloned relationship
	 */
	protected abstract E cloneIntermediaryRelationship(E intermediaryRelationship);
	
	/**
	 * Adapts the first abstract member of the provided intermediary relationship 
	 * by replacing it with one of its non abstract subclasses.
	 * @param intermediaryRelationship the intermediary relationship whose 
	 * first abstract member end will be adapted by this method
	 * @param abstractMember the first abstract member of the provided 
	 * intermediary relationship whose corresponding member end 
	 * will be adapted by this method
	 * @param subClass one of the non abstract subclasses of the provided abstract member 
	 * of the provided intermediary relationship, that will replace the abstract member in the
	 * relationship returned by this method
	 * @return the relationship obtained from the provided enqueued intermediary relationship
	 * and whose first abstract member is adapted by replacing it with the provided non abstract
	 * subclass
	 */
	protected abstract E adaptIntermediaryRelationship(E intermediaryRelationship, Class abstractMember, Class subClass);
	
	/**
	 * Sets the name and the owning package for each target relationship
	 * in the provided list of target relationships.
	 * The owning package is the same one owning the source relationship
	 * with abstract members to adapt, whereas the name conforms to 
	 * the following convention &lt;sourceRelationshipName&gt;-&lt;newNonAbstractMemberName&gt;
	 * @param newOwnedRelationships the list of target relationships
	 * whose names and owning packages are to be set by this method
	 */
	protected abstract void initTargetRelationshipsNamesAndOwnership(List<E> newOwnedRelationships);
	
	/**
	 * Removes the source relationship with abstract members and the intermediary relationships
	 * with abstract members from the model once its target relationships with 
	 * no abstract members have been created and added to the model to replace it.
	 */
	@Override
	public void postTransform(E source) {
		relationshipsToClean
		.stream()
		.forEach(E::destroy);
	}
}

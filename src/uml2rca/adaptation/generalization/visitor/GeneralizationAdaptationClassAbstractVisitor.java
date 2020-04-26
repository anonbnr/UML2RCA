package uml2rca.adaptation.generalization.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Class;

import core.conflict.AbstractConflictCandidate;
import core.conflict.AbstractConflictResolutionStrategy;
import core.conflict.AbstractConflictScope;
import core.conflict.IConflictDomain;
import core.conflict.IConflictResolutionStrategyType;
import uml2rca.java.uml2.uml.extensions.visitor.IUMLElementVisitor;

public abstract class GeneralizationAdaptationClassAbstractVisitor<E> implements IUMLElementVisitor, IConflictDomain<Class, E> {
	
	/* ATTRIBUTES */
	protected GeneralizationAdaptationClassVisitor sourceClassVisitor;
	protected E visitedElement;
	protected AbstractConflictScope<Class, E> conflictScope;
	protected AbstractConflictCandidate<Class, E> conflictCandidate;
	protected AbstractConflictResolutionStrategy<Class, E> conflictStrategy;
	protected IConflictResolutionStrategyType conflictStrategyType;
	protected List<E> toClean;

	/* CONSTRUCTOR */
	public GeneralizationAdaptationClassAbstractVisitor(GeneralizationAdaptationClassVisitor sourceClassVisitor,
			IConflictResolutionStrategyType conflictStrategyType) {
		
		this.sourceClassVisitor = sourceClassVisitor;
		this.conflictStrategyType = conflictStrategyType;
		toClean = new ArrayList<>();
	}
	
	/* METHODS */
	public E getVisitedElement() {
		return visitedElement;
	}
	
	public GeneralizationAdaptationClassVisitor getSourceClassVisitor() {
		return sourceClassVisitor;
	}
	
	@Override
	public AbstractConflictScope<Class, E> getConflictScope() {
		return conflictScope;
	}
	
	@Override
	public AbstractConflictCandidate<Class, E> getConflictCandidate() {
		return conflictCandidate;
	}
	
	@Override
	public AbstractConflictResolutionStrategy<Class, E> getConflictStrategy() {
		return conflictStrategy;
	}
	
	@Override
	public IConflictResolutionStrategyType getConflictStrategyType() {
		return conflictStrategyType;
	}
	
	public List<E> getToClean() {
		return toClean;
	}
	
	protected abstract boolean toClean(E element);
}

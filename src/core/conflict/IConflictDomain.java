package core.conflict;

import uml2rca.adaptation.generalization.conflict.resolution_strategy.IConflictResolutionStrategyType;

public interface IConflictDomain<T, E> {
	AbstractConflictScope<T, E> getConflictScope();
	AbstractConflictCandidate<T, E> getConflictCandidate();
	AbstractConflictResolutionStrategy<T, E> getConflictStrategy();
	IConflictResolutionStrategyType getConflictStrategyType();
}
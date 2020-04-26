package uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy;

import uml2rca.adaptation.generalization.conflict.resolution_strategy.IConflictResolutionStrategyType;

public enum DependencyConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

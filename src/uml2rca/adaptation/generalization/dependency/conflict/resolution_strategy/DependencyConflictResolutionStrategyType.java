package uml2rca.adaptation.generalization.dependency.conflict.resolution_strategy;

import core.conflict.IConflictResolutionStrategyType;

public enum DependencyConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

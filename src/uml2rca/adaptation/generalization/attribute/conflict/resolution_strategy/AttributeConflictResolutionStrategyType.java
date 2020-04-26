package uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy;

import core.conflict.IConflictResolutionStrategyType;

public enum AttributeConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

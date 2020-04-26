package uml2rca.adaptation.generalization.attribute.conflict.resolution_strategy;

import uml2rca.adaptation.generalization.conflict.resolution_strategy.IConflictResolutionStrategyType;

public enum AttributeConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

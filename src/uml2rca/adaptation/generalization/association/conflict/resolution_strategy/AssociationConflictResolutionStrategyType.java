package uml2rca.adaptation.generalization.association.conflict.resolution_strategy;

import core.conflict.IConflictResolutionStrategyType;

public enum AssociationConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

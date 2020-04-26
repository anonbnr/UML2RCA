package uml2rca.adaptation.generalization.association.conflict.resolution_strategy;

import uml2rca.adaptation.generalization.conflict.resolution_strategy.IConflictResolutionStrategyType;

public enum AssociationConflictResolutionStrategyType implements IConflictResolutionStrategyType {
	DEFAULT_RENAME,
	EXPERT_RENAME,
	DISCARD,
	NONE
}

1. Adaptations:
    - Multi-valued attributes
      1. nominal scaling
      1. ordinal scaling
      1. interordinal scaling
1. Conversions:
    - migration of Bidirectional Associations
    - migration of Class Associations
    - migration of N-ary Associations
    - migration of Generalizations
    - migration of multi-valued attributes
1. Project Refactoring:
    - create a separate maven project for the core generic metamodel transformation architecture in the umlrca project (2)
1. Model Manager
    - add papyrus exportation capabilities (1)
1. Documentation
    - check existing documentation and modify that which needs modification (1)
    - add missing documentation (1)
1. Documents
    - make UML viewpoint for core aspect of the metamodel (1)
    - make UML viewpoint for Formal Contexts (1)
    - make UML viewpoint for Relational Contexts (1)
    - make a README for how to use the tool on the git repo (1)
    - make UML for UML2RCA use-case:
        1. Viewpoint Adaptations (1)
        1. Viewpoint Conversion (1)
        1. Viewpoint ModelManager (1)
1. CLI:
    - make CLI tool to use the architecture (1)
1. Exceptions
    - refactor exceptions (2)
1. Tests
    - refactor AssociationWithAbstractMembersAdaptationTest (2) :
      1. describedIn -> N-ary association with abstract ends
      1. ImportedTo -> association class with abstract ends
      1. basedOn -> Reflexive association with abstract ends
      1. attacks -> association between types in the same generalization with abstract ends
    - include unit tests for all exceptions (2) :
      1. AttributeConflictResolutionStrategyException
      1. ConflictingNamesAndProvidedNamesSizesMismatchException
      1. NoModelStateFoundException
      1. NotADependencyWithAnAbstractMemberException
      1. NotALeafInGeneralizationHierarchyException
      1. NotAnAssociationWithAnAbstractMemberException
      1. NotATypException
      1. NotAValidLevelForGeneralizationAdaptationException
1. General
    - refactor all loops that can be written in streams (3)
    - add preTransform() and postTransform() to the ITransformation interface
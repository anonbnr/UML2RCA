# Introduction
This project defines UML-to-RCA metamodel transformations. It introduces a generic metamodel transformation architecture and specializes it into the UML-to-RCA use-case. It then uses data provided by the Knowmana project to validate both the architecture and the defined transformations. It relies on the RCA metamodel (*as provided by the rca project available at* https://github.com/anonbnr/rca) and the UML2 metamodel (*as provided by the* `org.eclipse.uml2.uml` *package*).

# Environment Configuration
## Eclipse IDE and other required tools
1. install `Eclipse IDE` pre-bundled with the Eclipse Modeling Tools: https://www.eclipse.org/downloads/packages/release/2020-03/r/eclipse-modeling-tools
1. install `Apache Maven` to automatically handle dependencies, as follows: `Help` -> `Install New Software` -> add a repository named "**m2eclipse**" at "http://download.eclipse.org/technology/m2e/releases" -> install `Maven Integration for Eclipse` -> `Restart Eclipse`
1. install the `Papyrus` project must to create, visualize, import and export the `.uml` model files, instead of using the `Ecore Modeling Editor`, as follows: `Help` -> `Eclipse Marketplace` -> search for `Papyrus Software Designer` in `All Available Markets` -> install `Papyrus Software Designer 1.1.0` -> `Restart Eclipse`.

## Project dependencies
### The RCA metamodel project
The RCA metamodel project must be imported into the workspace as follows:

1. `Window` -> `Perspective` -> `Open Perspective` -> `Other...` -> Choose `Git` -> right click on the `Git repositories` perspective -> `Clone a Git Repository...` -> copy https://github.com/anonbnr/rca.git into the `URI` field -> `Next` -> Enter GitHub credentials -> `Next` -> Select the proper directory to import the project -> `Finish` -> Enter GitHub credentials
1. `Window` -> `Perspective` -> `Open Perspective` -> `Java` -> right click on the `Package Explorer` perspective -> `Import...` -> `Git` -> `Projects from Git` -> `Next` -> `Existing local repository` -> `rca` -> `Next` -> `Next` -> `Finish`

### The UML2RCA project
The UML2RCA project must be imported into the workspace as follows:

1. `Window` -> `Perspective` -> `Open Perspective` -> `Other...` -> Choose `Git` -> right click on the `Git repositories` perspective -> `Clone a Git Repository...` -> copy https://github.com/anonbnr/UML2RCA.git into the `URI` field -> `Next` -> Enter GitHub credentials -> `Next` -> Select the proper directory to import the project -> `Finish` -> Enter GitHub credentials
1. `Window` -> `Perspective` -> `Open Perspective` -> `Java` -> right click on the `Package Explorer` perspective -> `Import...` -> `Git` -> `Projects from Git` -> `Next` -> `Existing local repository` -> `UML2RCA` -> `Next` -> `Next` -> `Finish`

## Creating a new project using UML2RCA and RCA
Once Eclipse IDE and its required tools have been installed, and the RCA metamodel and UML2RCA projects have been imported into the workspace, one can create a modeling project that uses them in its code, as follows:

1. `File` -> `New` -> `Other...` -> `Eclipse Modeling Framework` -> `Ecore Modeling Project`, then follow the installation wizard.
1. right click on the created project -> `Configure` -> `Convert to Maven Project` -> fill the fields of the Maven project to be created, mainly `GroupdId`, `ArtifactId`, `Version`, `Packaging` and optionally `Name` and `Description` -> `Finish`
1. open `pom.xml` and add the following at the end of the `<project></project>` root tag:

```xml
<!-- pom.xml of the modeling project -->
<project>
...
<dependencies>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13</version>
    <scope>test</scope>
  </dependency>

  <dependency>
    <groupId>org</groupId>
    <artifactId>rca</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>

  <dependency>
    <groupId>org</groupId>
    <artifactId>uml2rca</artifactId>
    <version>0.0.1-SNAPSHOT</version>
  </dependency>
</dependencies>
...
</project>
```

### Notes
1. If `Eclipse` got stuck upon adding the dependencies in the project's `pom.xml`, you can add the dependencies in the file outside `Eclipse` then **refresh the project** from within `Eclipse`.
1. Anytime an error arises from using an unrecognized element in the project, click `fix project setup...` in the proposed solutions, and choose the proposed project setup fix (*e.g. add* `X` *to required bundles*).

# FCA (Formal Context Analysis)
## Introduction
**FCA** is a data analysis and classification method used in many domains of computer sciences (*e.g. knowledge representation, knowledge engineering, information retrieval, supervised/unsupervised machine learning,* $\dots$), based on the mathematical theories of order and lattices. It is mainly used to formally represent the notions of formal contexts and formal concepts.

## Formal contexts
A **formal context** $K$ is a triplet $(O, A, I)$, where $O$ is a **set of objects**, $A$ is a **set of boolean formal attributes**, and $I \subseteq O \times A$ is a **binary incidence relation**, such that every couple $(o, a)$ belonging to $I$ indicates that the object $o$ has the attribute $a$.

!["Formal Context Example"](resources/images/readme/fca/formal_context.png "Formal Context Example")

## Formal concepts
Given a formal context $K = (O, A, I)$, a **formal concept** $C$ of $K$ is a couple $(E, I)$, where $E \subseteq O$ is called the **extension** of the concept, and $I \subseteq A$ is called the **intension** of the concept. More specifically, $C$ represents a maximum set of objects sharing a maximum set of attributes. Moreover, the set of all formal concepts associated to $K$ is denoted by $C_K$.

!["Formal Concept Example"](resources/images/readme/fca/formal_concept.png "Formal Concept Example")

## Subsumption relation
Given a formal context $K = (O, A, I)$, **FCA** defines a **subsumption relation**, designating a **partial order relation**, denoted by $\leq_S$, over the set of all the formal concepts associated to it $C_K$. The order is based on the set inclusion of the concepts' extensions and dualistically on the inverse set inclusion of the concepts' intensions. It can also be interpreted as a formal concept specialization/generalization such that:

1. a concept is considered **more general** than another concept if it contains more objects in its extension, while sharing a reduced number of attributes.
1. a concept is considered **more specific** than another concept if it contains less objects in its extension, while sharing a larger number of attributes.

!["Subsumption Relation Example"](resources/images/readme/fca/subsumption_relation.png "Subsumption Relation Example")

## Concept lattices
Given a formal context $K = (O, A, I)$, the subsumption relation $\leq_S$ over $C_K$ organizes the formal concepts in a **complete lattice** called a **concept lattice** or **Galois lattice**, denoted by $(C_k, \leq_S)$. It relies on a simplified attribute/object inheritance between the lattice's concepts.

!["Object and Attribute Inheritance Example"](resources/images/readme/fca/attribute_object_inheritance.png "Object and Attribute Inheritance Example")

!["concept lattice Example"](resources/images/readme/fca/concept_lattice.png "concept lattice Example")

## Attribute/Object-introducing and neutral concepts
An **object-introducing concept** is a concept having at least one object in its **simplified extension**, whereas an **attribute-introducing concept** is one having at least one object in its **simplified extension**. On the other hand, A **neutral concept** is one that is neither an object-introducing concept nor an attribute-introducing concept.

Furthermore, given a formal context $K = (O, A, I)$, the set of all object-introducing concepts of $K$ is denoted by $OC_K$, whereas its set of all attribute-introducing concepts is denoted by $AC_K$.

## OC-posets, AC-posets and AOC-posets
Given that the number of concepts in a concept lattice for a formal context increases exponentially with an increase in the sizes of the provided input sets of objects and attributes, it could be useful to represent the concepts hierarchy using other hierarchical structures of representation. In particular, such structures are partially ordered sets (or **posets**), designating suborders that exclude neutral concepts from the complete concept lattice, and that don't necessarily constitute concept lattices themselves. Examples of such posets include:

1. `OC-posets` (**Object-introducing Concept partially ordered sets**): the suborder underlying the set of object-introducing concepts.
1. `AC-posets` (**Attribute-introducing Concept partially ordered sets**): the suborder underlying the set of attribute-introducing concepts.
1. `AC-posets` (**Attribute-Object-introducing Concept partially ordered sets**): the suborder underlying the set of attribute-introducing and object-introducing concepts.

!["AOC-poset Example"](resources/images/readme/fca/aoc_poset.png "AOC-poset Example")

# RCA (Relational Context Analysis)
## Introduction
**RCA** is an extension of **FCA** for relational datasets. It allows taking relationships between different categories of objects into consideration throughout the analysis, where each object category is designated by a **formal context** whose concepts are represented by a **concept lattice**, and each relationship is materialized through a **relational context**, designating directed binary links between two concept lattices. The related concept lattices represent the source and target formal contexts of the corresponding relational context.

Underlying RCA is an iterative process aiming to refine the relationships between concept lattices, representing source and target formal contexts of relational contexts, by scaling the relational contexts using **scaling operators**. As a consequence, the scaling operation generates **relational attributes** for each iteration that are added to source formal contexts, and allows to discover new concepts, until no new concept can be discovered. At the end of the process, a set of interconnected concept lattices is obtained, which can be represented in a hierarchy of ontological concepts that and can be examined and analyzed using some knowledge representation formalism (*e.g. description logic*).

RCA can be very useful in software engineering, knowledge representation, artificial intelligence, $\dots$ Some applications include refactoring UML class and use case diagrams, extracting OO architectures, web service classification, construction and extraction of ontologies, $\dots$

## Relational contexts
A relational context $R$ is a triplet $(O_1, O_2, I)$ where $O_1$ is a set of objects originating from a formal context $K_1 = (O_1, A_1, I_1)$ called $R$'s **source context**, $O_2$ is a set of objects originating from a formal context $K_2 = (O_2, A_2, I_2)$ called $R$'s **target context**, and where $I \subseteq O_1 \times O_2$ is a **binary incidence relation**, such that every couple $(o_1, o_2)$ belonging to $I$ indicates that the object $o_1$ is related to the object $o_2$.

### Note
The source and target contexts of a relational context can designate the same formal context.

## RCFs (Relational Context Families)
An RCF is a couple $(K, R)$, where $K$ is a set of formal contexts, and $R$ is a set of relational contexts relating formal contexts from $K$.

!["Example of an RCF model"](resources/images/readme/rca/rcf_relational_model.png "Example of an RCF model")

!["Formal Contexts of the RCF example"](resources/images/readme/rca/rcf_formal_contexts.png "Formal Contexts of the RCF example")

!["Relational Contexts of the RCF example"](resources/images/readme/rca/rcf_relational_contexts.png "Relational Contexts of the RCF example")

## Relational Scaling
Given an RCF $(K, R)$, we can refine the relationships between the concept lattices representing the concepts of its formal contexts, by scaling its relational contexts using **scaling operators**. For each relational context $R_i = (O_j, O_k, I_i) \in R$, we define a **scaled relational context** $R_i^* = (O_j, C_{K_k}, CI_i)$, where:

1. $O_j$ is the set of objects of $R_i$'s source formal context $K_i \in K$;
1. $C_{K_k}$ is the set of concepts of the concept lattice built on the set of objects of $R_i$'s target formal context $K_k \in K$;
1. $CI_i \subseteq O_j \times C_{K_k}$ is an **incidence binary relationship**, such that every couple $(o, c) \in CI_i \iff S(R_i(o), Extent(c))$ is true;
1. $S$ is a **scaling operator**;
1. $R_i(o)$ is the set of objects in $O_k$, such that $\forall o' in O_k, (o, o') \in I_i$.

### Scaling operators & relational attributes
A **scaling operator** is an operator applied on a relational context to scale, that generates relational attributes in its source formal context, allowing to discover new formal concepts therein iteratively, and therefore refine the relationship between the concept lattices of the source and target formal contexts of the scaled relational context, until no new concepts can be discovered. In particular, a scaling operator is a predicate having an object, a relational context to scale, and a concept as input, and indicating whether the object and the concept are related by the scaled-up binary incidence relationship of the scaled relational context.

**Relational attributes** are binary attributes generated by scaling a relational context using a scaling operator, and having the generic syntax: `S [arg] r : c`, where:

1. $S$ is a scaling operator;
1. $arg$ is an optional argument specific to $S$;
1. $r$ is the relational context scaled by $S$;
1. $c$ is a concept in the set of concepts of the concept lattice built on the set of objects of $r$'s source target formal context.

### Types of scaling operators

| Operator     | Relational Attribute Syntax     | Predicate Condition     |
| :------------- | :------------- | :------------- |
| Universal (wide)       | $\forall r : c$       | $r(o) \subseteq Extension(c)$       |
| Universal (strict)       | $\forall \exists r : c$       | $r(o) \subseteq Extension(c)$ and $r(o) \neq \varnothing$       |
| Existential       | $\exists r : c$       | $r(o) \cap Extension(c) \neq \varnothing$       |
| Includes       | $\supseteq r : c$       | $r(o) \supseteq Extension(c)$       |
| Cardinality restriction       | $\geq n r : c$       | $r(o) \subseteq Extension(c)$ and $|r(o)| \geq n$      |
| $\dots$       | $\dots$       | $\dots$       |

### Properties of relational scaling
1. the homogeneity of concept descriptions is preserved, since all attributes (formal and relational attributes) are considered binary;
1. the standard algorithms for building concept lattices can be directly reused throughout the iterative RCA process.

### Example of relational scaling: The existential scaling operator
Given an RCF $(K, R)$, for each relational context $R_i = (O_j, O_k, I_i) \in R$, we define a **scaled relational context** $R_i^* = (O_j, C_{K_k}, CI_i)$, using the existential scaling operator, as follows:

1. $CI_i \subseteq O_j \times C_{K_k}$ is an **incidence binary relationship**, such that every couple $(o, c) \in CI_i \iff S(R_i(o), Extent(c))$ is true;
1. $S(R_i(o), Extent(c))$ is true $\iff \exists x \in R(o), x \in Extent(c)$

The first step consists of using the RCA's formal contexts to create their initial concept lattices. Afterwards, for each iteration $i$, we apply the existential scaling operator to scale the RCA's relational contexts, in order to generate the relational attributes, discover new concepts to refine the concept lattices of the iteration $i - 1$, and obtain the new refined concept lattices of the iteration $i$. Once no new concepts can be discovered, the process terminates and we are left with a set of interconnected and refined concept lattices upon which we can apply knowledge exploration algorithms (*among others*).

!["Initial concept lattices of the RCF example"](resources/images/readme/rca/rca_iterative_approach_initial_lattices.png "Initial concept lattices of the RCF example")

!["Concept lattice obtained after the first iteration of scaling the relational context 'eats'"](resources/images/readme/rca/rca_existential_scaling_eats_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'eats'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'contains'"](resources/images/readme/rca/rca_existential_scaling_contains_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'contains'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'producedIn'"](resources/images/readme/rca/rca_existential_scaling_producedIn_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'producedIn'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'hasForNational'"](resources/images/readme/rca/rca_existential_scaling_hasForNational_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'hasForNational'")

!["Concept lattices obtained after the second iteration of scaling all relational contexts"](resources/images/readme/rca/rca_existential_scaling_concept_lattices_step2.png "Concept lattices obtained after the second iteration of scaling all relational contexts")

!["Concept lattices obtained after the third iteration of scaling all relational contexts"](resources/images/readme/rca/rca_existential_scaling_concept_lattices_step3.png "Concept lattices obtained after the third iteration of scaling all relational contexts")

## RCA iterative algorithm

---
header-includes:
    - \usepackage[ruled,vlined,linesnumbered,nofillcomment]{algorithm2e}
---
\begin{algorithm}[H]
\caption{Multi-FCA}
\SetKwData{Halt}{halt}
\SetKwFunction{BuildLat}{BUILD-LATTICE}
\SetKwFunction{UpdateLat}{UPDATE-LATTICE}
\SetKwFunction{ExtendRel}{EXTEND-REL}
\SetKwFunction{Iso}{ISOMORPHIC}
\KwIn{An RCF (K, R)}
\KwResult{A family of 1 to n interconnected concept lattices}
\BlankLine
\Begin{
  $p \leftarrow$ 0\;
  \Halt $\leftarrow$ false\;
  \For{$i\leftarrow 1$ \KwTo $n$}{
    $\mathbb{L}^0[i] \leftarrow$ \BuildLat{$\mathbb{K}^0_i$}\;
  }
  \While{not \Halt}{
    $p ++$\;
    \For{$i\leftarrow 1$ \KwTo $n$}{
      $\mathbb{K}^p_i \leftarrow$ \ExtendRel{$\mathbb{K}^{p - 1}_i$, $\mathbb{L}^{p - 1}$}\;
      $\mathbb{L}^p[i] \leftarrow$ \UpdateLat{$\mathbb{K}^p_i$, $\mathbb{L}^{p - 1}[i]$}\;
    }
    \Halt $\leftarrow \bigwedge_{i=1}^{n}$ \Iso{$\mathbb{L}^p[i]$, $\mathbb{L}^{p - 1}[i]$}\;
  }
}
\end{algorithm}

# The RCA Metamodel
Based on the brief aforementioned introduction to RCA, the following metamodel has been crafted to describe its entities and associations.

## The Core viewpoint
*TODO* *TODO*

### Description
*TODO*

## The Formal Context viewpoint
*TODO*

### Description
*TODO*

## The Relation Context viewpoint
*TODO*

### Description
*TODO*

## The Global viewpoint

![The Global viewpoint of the RCA Metamodel](resources/images/readme/rca/RCF_MM.png "The Global viewpoint of the RCA Metamodel")

# The Generic Metamodel Transformation Architecture
## Introduction
*TODO*

![The Generic Metamodel Transformation Architecture ](resources/images/readme/mm_transformation_architecture/transformation_architecture.jpg "The Generic Metamodel Transformation Architecture")

## Transformations, Transformation strategies, and Transformers
*TODO*

## Adaptations, Adaptation strategies, and Adapters
*TODO*

## Conversions, Conversion strategies, and Converters
*TODO*

## Model Management
*TODO*

## Conflict Management
*TODO*

# The UML-to-RCA use-case
## Introduction
*TODO*

## Model Management
*TODO*

### Ecore Model Manager
*TODO*

### Ecore Model State
*TODO*

## Adaptations (X-UML to R-UML)
*TODO*

### Associations & Dependencies
*TODO*

#### Aggregations
*TODO*

#### Compositions
*TODO*

#### Dependencies
*TODO*

#### Association classes
*TODO*

#### Bidirectional associations
*TODO*

#### N-ary associations
*TODO*

##### The Materialization solution
*TODO*

##### The Forgotten solution
*TODO*

#### Associations with abstract members
*TODO*

#### Dependencies with abstract members
*TODO*

### Generalizations
*TODO*

#### Simple Generalizations
*TODO*

#### Multiple Generalizations
*TODO*

#### Visitors
*TODO*

#### Conflict Management
*TODO*

## Conversions (R-UML to unpopulated RCFs)
*TODO*

### Associations to Relational Contexts
*TODO*

### Concrete Classes to Formal Contexts
*TODO*

### Boolean Attributes to Formal Attributes
*TODO*

## Instance migrations (populating the RCFs)
*TODO*

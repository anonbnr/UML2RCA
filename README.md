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
**FCA** is a data analysis and classification method used in many domains of computer sciences (*e.g. knowledge representation, knowledge engineering, information retrieval, supervised/unsupervised machine learning, etc.*), based on the mathematical theories of order and lattices. It is mainly used to formally represent the notions of formal contexts and formal concepts.

## Formal contexts
A formal context <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is a triplet <img src="/tex/20d001cd101a48aafa186cde6c300ce2.svg?invert_in_darkmode&sanitize=true" align=middle width=61.23738884999998pt height=24.65753399999998pt/>, where <img src="/tex/9afe6a256a9817c76b579e6f5db9a578.svg?invert_in_darkmode&sanitize=true" align=middle width=12.99542474999999pt height=22.465723500000017pt/> is a **set of objects**, <img src="/tex/53d147e7f3fe6e47ee05b88b166bd3f6.svg?invert_in_darkmode&sanitize=true" align=middle width=12.32879834999999pt height=22.465723500000017pt/> is a **set of boolean formal attributes**, and <img src="/tex/11d582ed4f591b6841f59c448c555caf.svg?invert_in_darkmode&sanitize=true" align=middle width=75.84901004999999pt height=22.465723500000017pt/> is a **binary incidence relation**, such that every couple <img src="/tex/fa860135e72a41d843d95b8565a1faae.svg?invert_in_darkmode&sanitize=true" align=middle width=36.74852114999999pt height=24.65753399999998pt/> belonging to <img src="/tex/21fd4e8eecd6bdf1a4d3d6bd1fb8d733.svg?invert_in_darkmode&sanitize=true" align=middle width=8.515988249999989pt height=22.465723500000017pt/> indicates that the object <img src="/tex/7e1096128b080021db736ec4d7400387.svg?invert_in_darkmode&sanitize=true" align=middle width=7.968051299999991pt height=14.15524440000002pt/> has the attribute <img src="/tex/44bc9d542a92714cac84e01cbbb7fd61.svg?invert_in_darkmode&sanitize=true" align=middle width=8.68915409999999pt height=14.15524440000002pt/>.

!["Formal Context Example"](resources/images/readme/fca/formal_context.png "Formal Context Example")

## Formal concepts
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, a formal concept <img src="/tex/9b325b9e31e85137d1de765f43c0f8bc.svg?invert_in_darkmode&sanitize=true" align=middle width=12.92464304999999pt height=22.465723500000017pt/> of <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is a couple <img src="/tex/de5f4f310c82ee793dfb5ca26a568a63.svg?invert_in_darkmode&sanitize=true" align=middle width=41.68945934999999pt height=24.65753399999998pt/>, where <img src="/tex/a1889ce3b3d418272d5b3b0b5a261b31.svg?invert_in_darkmode&sanitize=true" align=middle width=47.995232999999985pt height=22.465723500000017pt/> is called the **extension** of the concept, and <img src="/tex/7b31cf7d5b4a2db65cf3020c1a34aaf6.svg?invert_in_darkmode&sanitize=true" align=middle width=42.76239494999999pt height=22.465723500000017pt/> is called the **intension** of the concept. More specifically, <img src="/tex/9b325b9e31e85137d1de765f43c0f8bc.svg?invert_in_darkmode&sanitize=true" align=middle width=12.92464304999999pt height=22.465723500000017pt/> represents a maximum set of objects sharing a maximum set of attributes. Moreover, the set of all formal concepts associated to <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is denoted by <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/>.

!["Formal Concept Example"](resources/images/readme/fca/formal_concept.png "Formal Concept Example")

## Subsumption relation
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, **FCA** defines a partial order relation, denoted by <img src="/tex/536f7120d6719f06ec560b97fce9b782.svg?invert_in_darkmode&sanitize=true" align=middle width=21.48638414999999pt height=20.908638300000003pt/>, over the set of all the formal concepts associated to it <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/>. The order is based on the set inclusion of the concepts' extensions and dualistically on the inverse set inclusion of the concepts' intensions. It can also be interpreted as a formal concept specialization/generalization such that:

1. a concept is considered **more general** than another concept if it contains more objects in its extension, while sharing a reduced number of attributes.
1. a concept is considered **more specific** than another concept if it contains less objects in its extension, while sharing a larger number of attributes.

!["Subsumption Relation Example"](resources/images/readme/fca/subsumption_relation.png "Subsumption Relation Example")

## Concepts lattices
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, the subsumption relation <img src="/tex/536f7120d6719f06ec560b97fce9b782.svg?invert_in_darkmode&sanitize=true" align=middle width=21.48638414999999pt height=20.908638300000003pt/> over <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/> organizes the formal concepts in a complete lattice called a **concepts lattice** or **Galois lattice**, denoted by <img src="/tex/642525c6b2647ccf33e1b3d8b33fe1a9.svg?invert_in_darkmode&sanitize=true" align=middle width=62.236407749999984pt height=24.65753399999998pt/>. It relies on a simplified attribute/object inheritance between the lattice's concepts.

!["Object and Attribute Inheritance Example"](resources/images/readme/fca/attribute_object_inheritance.png "Object and Attribute Inheritance Example")

!["Concepts Lattice Example"](resources/images/readme/fca/concept_lattice.png "Concepts Lattice Example")

## Attribute/Object-introducing and neutral concepts
An **object-introducing concept** is a concept having at least one object in its **simplified extension**, whereas an **attribute-introducing concept** is one having at least one object in its **simplified extension**. On the other hand, A **neutral concept** is one that is neither an object-introducing concept nor an attribute-introducing concept.

Furthermore, given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, the set of all object-introducing concepts of <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is denoted by <img src="/tex/b94cb59c77e85ea21fff50323865a06d.svg?invert_in_darkmode&sanitize=true" align=middle width=36.59594234999999pt height=22.465723500000017pt/>, whereas its set of all attribute-introducing concepts is denoted by <img src="/tex/36cef81fe35ab60415ea0b3e8b1b4873.svg?invert_in_darkmode&sanitize=true" align=middle width=35.92931594999999pt height=22.465723500000017pt/>.

## OC-poset, AC-poset and AOC-poset
Given that the number of concepts in a concepts lattice for a formal context increases exponentially with an increase in the sizes of the provided input sets of objects and attributes, it could be useful to represent the concepts hierarchy using other hierarchical structures of representation. In particular, such structures are partially ordered sets (or **posets**), designating suborders that exclude neutral concepts from the complete concepts lattice, and that don't necessarily constitute concepts lattices themselves. Examples of such posets include:

1. `OC-posets` (**Object-introducing Concept partially ordered sets**): the suborder underlying the set of object-introducing concepts.
1. `AC-posets` (**Attribute-introducing Concept partially ordered sets**): the suborder underlying the set of attribute-introducing concepts.
1. `AC-posets` (**Attribute-Object-introducing Concept partially ordered sets**): the suborder underlying the set of attribute-introducing and object-introducing concepts.

!["AOC-poset Example"](resources/images/readme/fca/aoc_poset.png "AOC-poset Example")

# RCA (Relational Context Analysis)
## Introduction
**RCA** is an extension of **FCA** (**Formal Context Analysis**) for relational datasets. It allows taking relationships between different categories of objects into consideration throughout the analysis, where each object category is represented by a **concept lattice**, and each relationship is represented by a directed binary link between two lattices, source and target. Relationships between lattices are materialized through relational attributes, integrated to source objects and pointing to concepts in the target lattices.

Underlying RCA is an iterative process aiming to refine the relationships between concept lattices, by adding relational attributes to objects of source lattices using **scaling operators**, and discovering new concepts at each iteration, until no new concept can be discovered. At the end of the process, a set of interconnected concept lattices is obtained, which can be represented in a hierarchy of ontological concepts that and can be examined and analyzed using some knowledge representation formalism (e.g. description logic).

RCA can be very useful in software engineering, knowledge representation, artificial intelligence, etc. Some applications include refactoring UML class and use case diagrams, extracting OO architectures, web service classification, construction and extraction of ontologies, etc.

## RCF (Relational Context Family)
*TODO*

## Relational Scaling
*TODO*

## Algorithm
*TODO*

# The RCA Metamodel
Based on the brief aforementioned introduction to RCA, the following metamodel has been crafted to describe its entities and associations.

## The Core viewpoint
*TODO*

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

![The Global viewpoint of the RCA Metamodel](resources/images/rca/RCF_MM.png "The Global viewpoint of the RCA Metamodel")

# The Generic Metamodel Transformation Architecture
## Introduction
*TODO*

![The Generic Metamodel Transformation Architecture ](resources/images/mm_transformation_architecture/transformation_architecture.jpg "The Generic Metamodel Transformation Architecture")

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

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
**FCA** is a data analysis and classification method used in many domains of computer sciences (*e.g. knowledge representation, knowledge engineering, information retrieval, supervised/unsupervised machine learning,* <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/>), based on the mathematical theories of order and lattices. It is mainly used to formally represent the notions of formal contexts and formal concepts.

## Formal contexts
A **formal context** <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is a triplet <img src="/tex/20d001cd101a48aafa186cde6c300ce2.svg?invert_in_darkmode&sanitize=true" align=middle width=61.23738884999998pt height=24.65753399999998pt/>, where <img src="/tex/9afe6a256a9817c76b579e6f5db9a578.svg?invert_in_darkmode&sanitize=true" align=middle width=12.99542474999999pt height=22.465723500000017pt/> is a **set of objects**, <img src="/tex/53d147e7f3fe6e47ee05b88b166bd3f6.svg?invert_in_darkmode&sanitize=true" align=middle width=12.32879834999999pt height=22.465723500000017pt/> is a **set of boolean formal attributes**, and <img src="/tex/11d582ed4f591b6841f59c448c555caf.svg?invert_in_darkmode&sanitize=true" align=middle width=75.84901004999999pt height=22.465723500000017pt/> is a **binary incidence relation**, such that every couple <img src="/tex/fa860135e72a41d843d95b8565a1faae.svg?invert_in_darkmode&sanitize=true" align=middle width=36.74852114999999pt height=24.65753399999998pt/> belonging to <img src="/tex/21fd4e8eecd6bdf1a4d3d6bd1fb8d733.svg?invert_in_darkmode&sanitize=true" align=middle width=8.515988249999989pt height=22.465723500000017pt/> indicates that the object <img src="/tex/7e1096128b080021db736ec4d7400387.svg?invert_in_darkmode&sanitize=true" align=middle width=7.968051299999991pt height=14.15524440000002pt/> has the attribute <img src="/tex/44bc9d542a92714cac84e01cbbb7fd61.svg?invert_in_darkmode&sanitize=true" align=middle width=8.68915409999999pt height=14.15524440000002pt/>.

!["Formal Context Example"](resources/images/readme/fca/formal_context.png "Formal Context Example")

## Formal concepts
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, a **formal concept** <img src="/tex/9b325b9e31e85137d1de765f43c0f8bc.svg?invert_in_darkmode&sanitize=true" align=middle width=12.92464304999999pt height=22.465723500000017pt/> of <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is a couple <img src="/tex/de5f4f310c82ee793dfb5ca26a568a63.svg?invert_in_darkmode&sanitize=true" align=middle width=41.68945934999999pt height=24.65753399999998pt/>, where <img src="/tex/a1889ce3b3d418272d5b3b0b5a261b31.svg?invert_in_darkmode&sanitize=true" align=middle width=47.995232999999985pt height=22.465723500000017pt/> is called the **extension** of the concept, and <img src="/tex/7b31cf7d5b4a2db65cf3020c1a34aaf6.svg?invert_in_darkmode&sanitize=true" align=middle width=42.76239494999999pt height=22.465723500000017pt/> is called the **intension** of the concept. More specifically, <img src="/tex/9b325b9e31e85137d1de765f43c0f8bc.svg?invert_in_darkmode&sanitize=true" align=middle width=12.92464304999999pt height=22.465723500000017pt/> represents a maximum set of objects sharing a maximum set of attributes. Moreover, the set of all formal concepts associated to <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is denoted by <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/>.

!["Formal Concept Example"](resources/images/readme/fca/formal_concept.png "Formal Concept Example")

## Subsumption relation
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, **FCA** defines a **subsumption relation**, designating a **partial order relation**, denoted by <img src="/tex/536f7120d6719f06ec560b97fce9b782.svg?invert_in_darkmode&sanitize=true" align=middle width=21.48638414999999pt height=20.908638300000003pt/>, over the set of all the formal concepts associated to it <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/>. The order is based on the set inclusion of the concepts' extensions and dualistically on the inverse set inclusion of the concepts' intensions. It can also be interpreted as a formal concept specialization/generalization such that:

1. a concept is considered **more general** than another concept if it contains more objects in its extension, while sharing a reduced number of attributes.
1. a concept is considered **more specific** than another concept if it contains less objects in its extension, while sharing a larger number of attributes.

!["Subsumption Relation Example"](resources/images/readme/fca/subsumption_relation.png "Subsumption Relation Example")

## Concept lattices
Given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, the subsumption relation <img src="/tex/536f7120d6719f06ec560b97fce9b782.svg?invert_in_darkmode&sanitize=true" align=middle width=21.48638414999999pt height=20.908638300000003pt/> over <img src="/tex/c36e2b23d98c6f456644cf99d478c7e0.svg?invert_in_darkmode&sanitize=true" align=middle width=23.600517599999993pt height=22.465723500000017pt/> organizes the formal concepts in a **complete lattice** called a **concept lattice** or **Galois lattice**, denoted by <img src="/tex/642525c6b2647ccf33e1b3d8b33fe1a9.svg?invert_in_darkmode&sanitize=true" align=middle width=62.236407749999984pt height=24.65753399999998pt/>. It relies on a simplified attribute/object inheritance between the lattice's concepts.

!["Object and Attribute Inheritance Example"](resources/images/readme/fca/attribute_object_inheritance.png "Object and Attribute Inheritance Example")

!["concept lattice Example"](resources/images/readme/fca/concept_lattice.png "concept lattice Example")

## Attribute/Object-introducing and neutral concepts
An **object-introducing concept** is a concept having at least one object in its **simplified extension**, whereas an **attribute-introducing concept** is one having at least one object in its **simplified extension**. On the other hand, A **neutral concept** is one that is neither an object-introducing concept nor an attribute-introducing concept.

Furthermore, given a formal context <img src="/tex/065079d916ca49732f03e9dbeaa64a3c.svg?invert_in_darkmode&sanitize=true" align=middle width=98.29201964999999pt height=24.65753399999998pt/>, the set of all object-introducing concepts of <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is denoted by <img src="/tex/b94cb59c77e85ea21fff50323865a06d.svg?invert_in_darkmode&sanitize=true" align=middle width=36.59594234999999pt height=22.465723500000017pt/>, whereas its set of all attribute-introducing concepts is denoted by <img src="/tex/36cef81fe35ab60415ea0b3e8b1b4873.svg?invert_in_darkmode&sanitize=true" align=middle width=35.92931594999999pt height=22.465723500000017pt/>.

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

RCA can be very useful in software engineering, knowledge representation, artificial intelligence, <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/> Some applications include refactoring UML class and use case diagrams, extracting OO architectures, web service classification, construction and extraction of ontologies, <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/>

## Relational contexts
A relational context <img src="/tex/1e438235ef9ec72fc51ac5025516017c.svg?invert_in_darkmode&sanitize=true" align=middle width=12.60847334999999pt height=22.465723500000017pt/> is a triplet <img src="/tex/56e7b483c84ec35f0160e4be8fa10098.svg?invert_in_darkmode&sanitize=true" align=middle width=75.73966454999999pt height=24.65753399999998pt/> where <img src="/tex/622470071275f5029e81de42c58e8072.svg?invert_in_darkmode&sanitize=true" align=middle width=19.09133654999999pt height=22.465723500000017pt/> is a set of objects originating from a formal context <img src="/tex/790369db1888ac7f6827f12b756c0d55.svg?invert_in_darkmode&sanitize=true" align=middle width=124.86752024999998pt height=24.65753399999998pt/> called <img src="/tex/1e438235ef9ec72fc51ac5025516017c.svg?invert_in_darkmode&sanitize=true" align=middle width=12.60847334999999pt height=22.465723500000017pt/>'s **source context**, <img src="/tex/07e86e45b7ebe272704b69abf2775d41.svg?invert_in_darkmode&sanitize=true" align=middle width=19.09133654999999pt height=22.465723500000017pt/> is a set of objects originating from a formal context <img src="/tex/07df440a13390cc0ef2ea0cb53e38048.svg?invert_in_darkmode&sanitize=true" align=middle width=124.86752024999998pt height=24.65753399999998pt/> called <img src="/tex/1e438235ef9ec72fc51ac5025516017c.svg?invert_in_darkmode&sanitize=true" align=middle width=12.60847334999999pt height=22.465723500000017pt/>'s **target context**, and where <img src="/tex/5ac9c573049bc2fe01918d8797071db2.svg?invert_in_darkmode&sanitize=true" align=middle width=89.52937289999998pt height=22.465723500000017pt/> is a **binary incidence relation**, such that every couple <img src="/tex/06e07180bb596de40f20f4d1a010db06.svg?invert_in_darkmode&sanitize=true" align=middle width=50.77633769999999pt height=24.65753399999998pt/> belonging to <img src="/tex/21fd4e8eecd6bdf1a4d3d6bd1fb8d733.svg?invert_in_darkmode&sanitize=true" align=middle width=8.515988249999989pt height=22.465723500000017pt/> indicates that the object <img src="/tex/6ec463416edd47e15ded9380bc1cae48.svg?invert_in_darkmode&sanitize=true" align=middle width=14.52059729999999pt height=14.15524440000002pt/> is related to the object <img src="/tex/b9b618aebd7d7a95ad0f69db2c8539e7.svg?invert_in_darkmode&sanitize=true" align=middle width=14.52059729999999pt height=14.15524440000002pt/>.

### Note
The source and target contexts of a relational context can designate the same formal context.

## RCFs (Relational Context Families)
An RCF is a couple <img src="/tex/e90d2318e505da710480ffe360f0c48b.svg?invert_in_darkmode&sanitize=true" align=middle width=46.923541499999985pt height=24.65753399999998pt/>, where <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/> is a set of formal contexts, and <img src="/tex/1e438235ef9ec72fc51ac5025516017c.svg?invert_in_darkmode&sanitize=true" align=middle width=12.60847334999999pt height=22.465723500000017pt/> is a set of relational contexts relating formal contexts from <img src="/tex/d6328eaebbcd5c358f426dbea4bdbf70.svg?invert_in_darkmode&sanitize=true" align=middle width=15.13700594999999pt height=22.465723500000017pt/>.

!["Example of an RCF model"](resources/images/readme/rca/rcf_relational_model.png "Example of an RCF model")

!["Formal Contexts of the RCF example"](resources/images/readme/rca/rcf_formal_contexts.png "Formal Contexts of the RCF example")

!["Relational Contexts of the RCF example"](resources/images/readme/rca/rcf_relational_contexts.png "Relational Contexts of the RCF example")

## Relational Scaling
Given an RCF <img src="/tex/e90d2318e505da710480ffe360f0c48b.svg?invert_in_darkmode&sanitize=true" align=middle width=46.923541499999985pt height=24.65753399999998pt/>, we can refine the relationships between the concept lattices representing the concepts of its formal contexts, by scaling its relational contexts using **scaling operators**. For each relational context <img src="/tex/67f5bb4cc5d83694850f1bef2b8a2940.svg?invert_in_darkmode&sanitize=true" align=middle width=152.75948654999996pt height=24.65753399999998pt/>, we define a **scaled relational context** <img src="/tex/308664440e9d120c7b7c8dac4469711a.svg?invert_in_darkmode&sanitize=true" align=middle width=145.37172045pt height=24.65753399999998pt/>, where:

1. <img src="/tex/1bf3e050ffcb24b1420f157308722814.svg?invert_in_darkmode&sanitize=true" align=middle width=18.643298849999994pt height=22.465723500000017pt/> is the set of objects of <img src="/tex/6660896e4379722ff79bba94961b201c.svg?invert_in_darkmode&sanitize=true" align=middle width=17.132374049999992pt height=22.465723500000017pt/>'s source formal context <img src="/tex/059351998a7c2c0de998c68ae7a57715.svg?invert_in_darkmode&sanitize=true" align=middle width=54.66215204999998pt height=22.465723500000017pt/>;
1. <img src="/tex/dfab4f942dbc8acb7fde44e5856a065a.svg?invert_in_darkmode&sanitize=true" align=middle width=29.15882639999999pt height=22.465723500000017pt/> is the set of concepts of the concept lattice built on the set of objects of <img src="/tex/6660896e4379722ff79bba94961b201c.svg?invert_in_darkmode&sanitize=true" align=middle width=17.132374049999992pt height=22.465723500000017pt/>'s target formal context <img src="/tex/38813537534a43cbcde583e58161dfe4.svg?invert_in_darkmode&sanitize=true" align=middle width=57.27729974999999pt height=22.465723500000017pt/>;
1. <img src="/tex/3a234b695ffbd94730b984e883bfca93.svg?invert_in_darkmode&sanitize=true" align=middle width=116.25632534999998pt height=22.465723500000017pt/> is an **incidence binary relationship**, such that every couple <img src="/tex/799cbe027574757766878ecd80d648dd.svg?invert_in_darkmode&sanitize=true" align=middle width=270.88540215pt height=24.65753399999998pt/> is true;
1. <img src="/tex/e257acd1ccbe7fcb654708f1a866bfe9.svg?invert_in_darkmode&sanitize=true" align=middle width=11.027402099999989pt height=22.465723500000017pt/> is a **scaling operator**;
1. <img src="/tex/8b78add0b028080cbc2fe919157856e1.svg?invert_in_darkmode&sanitize=true" align=middle width=38.70775589999999pt height=24.65753399999998pt/> is the set of objects in <img src="/tex/d558846fb5620bb03a65a3cc2524dfed.svg?invert_in_darkmode&sanitize=true" align=middle width=19.80481964999999pt height=22.465723500000017pt/>, such that <img src="/tex/f61f485e6dc6f909cf861ebf7b367b86.svg?invert_in_darkmode&sanitize=true" align=middle width=137.78247449999998pt height=24.7161288pt/>.

### Scaling operators & relational attributes
A **scaling operator** is an operator applied on a relational context to scale, that generates relational attributes in its source formal context, allowing to discover new formal concepts therein iteratively, and therefore refine the relationship between the concept lattices of the source and target formal contexts of the scaled relational context, until no new concepts can be discovered. In particular, a scaling operator is a predicate having an object, a relational context to scale, and a concept as input, and indicating whether the object and the concept are related by the scaled-up binary incidence relationship of the scaled relational context.

**Relational attributes** are binary attributes generated by scaling a relational context using a scaling operator, and having the generic syntax: `S [arg] r : c`, where:

1. <img src="/tex/e257acd1ccbe7fcb654708f1a866bfe9.svg?invert_in_darkmode&sanitize=true" align=middle width=11.027402099999989pt height=22.465723500000017pt/> is a scaling operator;
1. <img src="/tex/1750b1973e060eadce3b5dbfd08a04d2.svg?invert_in_darkmode&sanitize=true" align=middle width=24.99248564999999pt height=14.15524440000002pt/> is an optional argument specific to <img src="/tex/e257acd1ccbe7fcb654708f1a866bfe9.svg?invert_in_darkmode&sanitize=true" align=middle width=11.027402099999989pt height=22.465723500000017pt/>;
1. <img src="/tex/89f2e0d2d24bcf44db73aab8fc03252c.svg?invert_in_darkmode&sanitize=true" align=middle width=7.87295519999999pt height=14.15524440000002pt/> is the relational context scaled by <img src="/tex/e257acd1ccbe7fcb654708f1a866bfe9.svg?invert_in_darkmode&sanitize=true" align=middle width=11.027402099999989pt height=22.465723500000017pt/>;
1. <img src="/tex/3e18a4a28fdee1744e5e3f79d13b9ff6.svg?invert_in_darkmode&sanitize=true" align=middle width=7.11380504999999pt height=14.15524440000002pt/> is a concept in the set of concepts of the concept lattice built on the set of objects of <img src="/tex/89f2e0d2d24bcf44db73aab8fc03252c.svg?invert_in_darkmode&sanitize=true" align=middle width=7.87295519999999pt height=14.15524440000002pt/>'s source target formal context.

### Types of scaling operators

| Operator     | Relational Attribute Syntax     | Predicate Condition     |
| :------------- | :------------- | :------------- |
| Universal (wide)       | <img src="/tex/497d8a09aec9b094afdc9cc8df03f48b.svg?invert_in_darkmode&sanitize=true" align=middle width=37.81762874999999pt height=22.831056599999986pt/>       | <img src="/tex/5192e9809ebd086f1934c591d614a966.svg?invert_in_darkmode&sanitize=true" align=middle width=147.5812173pt height=24.65753399999998pt/>       |
| Universal (strict)       | <img src="/tex/65807e77353f0a701cb860896383f97b.svg?invert_in_darkmode&sanitize=true" align=middle width=46.950076799999984pt height=22.831056599999986pt/>       | <img src="/tex/5192e9809ebd086f1934c591d614a966.svg?invert_in_darkmode&sanitize=true" align=middle width=147.5812173pt height=24.65753399999998pt/> and <img src="/tex/e22e592b4a8f56725ddbf24f110ff7ce.svg?invert_in_darkmode&sanitize=true" align=middle width=63.329503049999985pt height=24.65753399999998pt/>       |
| Existential       | <img src="/tex/c267acf8e0b07eb1994d16b159b9513d.svg?invert_in_darkmode&sanitize=true" align=middle width=37.81762874999999pt height=22.831056599999986pt/>       | <img src="/tex/b173170606ee124864b2c63a3de40fde.svg?invert_in_darkmode&sanitize=true" align=middle width=178.63134959999996pt height=24.65753399999998pt/>       |
| Includes       | <img src="/tex/6ec2b2a1d7c5aec7e7e1fadd1e946f5e.svg?invert_in_darkmode&sanitize=true" align=middle width=46.03671269999999pt height=20.908638300000003pt/>       | <img src="/tex/75751e7402d90296905e59cbe12d9e35.svg?invert_in_darkmode&sanitize=true" align=middle width=147.5812173pt height=24.65753399999998pt/>       |
| Cardinality restriction       | <img src="/tex/3034b67c564db6da36490a42f0e61304.svg?invert_in_darkmode&sanitize=true" align=middle width=55.903588949999985pt height=20.908638300000003pt/>       | <img src="/tex/5192e9809ebd086f1934c591d614a966.svg?invert_in_darkmode&sanitize=true" align=middle width=147.5812173pt height=24.65753399999998pt/> and <img src="/tex/d99a096952189f1e8a9f84fa04866767.svg?invert_in_darkmode&sanitize=true" align=middle width=69.54339479999999pt height=24.65753399999998pt/>      |
| <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/>       | <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/>       | <img src="/tex/76aacde399706233c450f7a48e28adb4.svg?invert_in_darkmode&sanitize=true" align=middle width=19.17798959999999pt height=14.15524440000002pt/>       |

### Properties of relational scaling
1. the homogeneity of concept descriptions is preserved, since all attributes (formal and relational attributes) are considered binary;
1. the standard algorithms for building concept lattices can be directly reused throughout the iterative RCA process.

### Example of relational scaling: The existential scaling operator
Given an RCF <img src="/tex/e90d2318e505da710480ffe360f0c48b.svg?invert_in_darkmode&sanitize=true" align=middle width=46.923541499999985pt height=24.65753399999998pt/>, for each relational context <img src="/tex/67f5bb4cc5d83694850f1bef2b8a2940.svg?invert_in_darkmode&sanitize=true" align=middle width=152.75948654999996pt height=24.65753399999998pt/>, we define a **scaled relational context** <img src="/tex/308664440e9d120c7b7c8dac4469711a.svg?invert_in_darkmode&sanitize=true" align=middle width=145.37172045pt height=24.65753399999998pt/>, using the existential scaling operator, as follows:

1. <img src="/tex/3a234b695ffbd94730b984e883bfca93.svg?invert_in_darkmode&sanitize=true" align=middle width=116.25632534999998pt height=22.465723500000017pt/> is an **incidence binary relationship**, such that every couple <img src="/tex/799cbe027574757766878ecd80d648dd.svg?invert_in_darkmode&sanitize=true" align=middle width=270.88540215pt height=24.65753399999998pt/> is true;
1. <img src="/tex/4cbed01254d3109d14f69c95d8027881.svg?invert_in_darkmode&sanitize=true" align=middle width=141.5960667pt height=24.65753399999998pt/> is true <img src="/tex/de4fe22cea7319711bbad46435d3cca2.svg?invert_in_darkmode&sanitize=true" align=middle width=219.8114919pt height=24.65753399999998pt/>

The first step consists of using the RCA's formal contexts to create their initial concept lattices. Afterwards, for each iteration <img src="/tex/77a3b857d53fb44e33b53e4c8b68351a.svg?invert_in_darkmode&sanitize=true" align=middle width=5.663225699999989pt height=21.68300969999999pt/>, we apply the existential scaling operator to scale the RCA's relational contexts, in order to generate the relational attributes, discover new concepts to refine the concept lattices of the iteration <img src="/tex/9fd14f2f99d0c59fb8839e136e9fc5a4.svg?invert_in_darkmode&sanitize=true" align=middle width=33.97362704999999pt height=21.68300969999999pt/>, and obtain the new refined concept lattices of the iteration <img src="/tex/77a3b857d53fb44e33b53e4c8b68351a.svg?invert_in_darkmode&sanitize=true" align=middle width=5.663225699999989pt height=21.68300969999999pt/>. Once no new concepts can be discovered, the process terminates and we are left with a set of interconnected and refined concept lattices upon which we can apply knowledge exploration algorithms (*among others*).

!["Initial concept lattices of the RCF example"](resources/images/readme/rca/rca_iterative_approach_initial_lattices.png "Initial concept lattices of the RCF example")

!["Concept lattice obtained after the first iteration of scaling the relational context 'eats'"](resources/images/readme/rca/rca_existential_scaling_eats_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'eats'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'contains'"](resources/images/readme/rca/rca_existential_scaling_contains_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'contains'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'producedIn'"](resources/images/readme/rca/rca_existential_scaling_producedIn_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'producedIn'")

!["Concept lattice obtained after the first iteration of scaling the relational context 'hasForNational'"](resources/images/readme/rca/rca_existential_scaling_hasForNational_step1.png "Concept lattice obtained after the first iteration of scaling the relational context 'hasForNational'")

!["Concept lattices obtained after the second iteration of scaling all relational contexts"](resources/images/readme/rca/rca_existential_scaling_concept_lattices_step2.png "Concept lattices obtained after the second iteration of scaling all relational contexts")

!["Concept lattices obtained after the third iteration of scaling all relational contexts"](resources/images/readme/rca/rca_existential_scaling_concept_lattices_step3.png "Concept lattices obtained after the third iteration of scaling all relational contexts")

## RCA iterative algorithm

!["RCA iterative algorithm"](resources/images/readme/rca/rca_iterative_algorithm.png "RCA iterative algorithm")

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

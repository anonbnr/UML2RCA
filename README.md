# Introduction
This project defines UML-to-RCA metamodel transformations. It introduces a generic metamodel transformation architecture and specializes it into the UML-to-RCA use-case. It then uses data provided by the Knowmana project to validate both the architecture and the defined transformations. It relies on the RCA metamodel (*as provided by the rca project available at* https://github.com/anonbnr/rca) and the UML2 metamodel (*as provided by the* `org.eclipse.uml2.uml` *package*).

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

![The Global viewpoint of the RCA Metamodel](resources/images/RCF_MM.png "The Global viewpoint of the RCA Metamodel")

# The Generic Metamodel Transformation Architecture
## Introduction
*TODO*

![The Generic Metamodel Transformation Architecture ](resources/images/transformation_architecture.jpg "The Generic Metamodel Transformation Architecture")

## Transformations, Transformation strategies, and Transformers
*TODO*

## Adaptations, Adaptation strategies, and Adapters
*TODO*

## Conversions, Conversion strategies, and Converters
*TODO*

## Model Management
*TODO*

# The UML-to-RCA use-case
## Introduction
*TODO*

## Adaptations (X-UML to R-UML)
*TODO*

## Conversions (R-UML to RCF)
*TODO*

## Instance migrations (populating the RCF)

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

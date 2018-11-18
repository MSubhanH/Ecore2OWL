# Ecore2OWL
The Ecore2OWL Transformation Project performs transformation from .ecore to .owl and backwards. It can be used for transformation between meta-models as well as between instances of both the domains. The key purpose of this project is to provide the user a framework with which transformation between both of these domains can happen easily with the requirement that changes in one domain shall be transformed into corresponding changes in the other domain. This shall allow the user to benefit from the data modeling flexibility provided in the .ecore domain along with the semantical reasoning capabilities of the .owl domain.

The emf4sw has beeen inherited from https://github.com/ghillairet/emf4sw and the atl-commons has been inherited from https://github.com/ghillairet/atl-commons and therefore are the original works of author Guillaume Hillairet. There are only minor modifications done to the emf4sw project.

The CardinaltyMapper project is an adapter code implementation for the domain elements that are missed by the emf4sw project.

The Ecore2OWL GUI is a simple GUI allowing the user to perform transformations interactively. 

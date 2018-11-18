# atl-commons

Effective writing of ATL model transformations' launchers in Java.

	register( UMLPackage.eINSTANCE.eResource() );
	
	Transformation<EMFModel, EMFModel> uml2ecore = new  Transformations.Builder()
	.asm("uml2ecore.asm")
	.in(get(UMLPackage.eNS_URI), "IN", "uml")
	.out(ecore(), "OUT", "ecore")
	.buildOneInOneOut();

	EMFModel out = transform(inject(resource("model.uml"), get(UMLPackage.eNS_URI)), uml2ecore);


eclipse update site http://code.google.com/a/eclipselabs.org/p/atl-commons/


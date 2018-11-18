package com.emf4sw.rdf.examples;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;

import com.atl.common.models.Models;
import com.emf4sw.rdf.transform.RDF2ModelGen;

public class GenerateRdf2Model {
	
	public static final String pathEcoreModel = "examples/com.emf4sw.rdf.examples/src/Family.ecore";
	public static final String pathATLFile = "examples/com.emf4sw.rdf.examples/src/rdf2family.atl";

	
	public static void main(String[] args) {
		
		RDF2ModelGen generator = new RDF2ModelGen(Models.resource(pathEcoreModel, true));
		Resource atl = generator.getResource();
		
		atl.setURI(URI.createURI(pathATLFile));
		try {
			atl.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

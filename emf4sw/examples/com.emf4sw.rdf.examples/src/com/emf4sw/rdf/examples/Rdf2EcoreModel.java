package com.emf4sw.rdf.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.atl.common.models.Models;
import com.emf4sw.rdf.resource.impl.TTLResourceFactory;
import com.emf4sw.rdf.transform.RDF2Model;

public class Rdf2EcoreModel {
	
	public static final String pathEcoreModel = "examples/com.emf4sw.rdf.examples/src/Family.ecore";
	public static final String pathEcoreModelInstance = "examples/com.emf4sw.rdf.examples/src/model.family.xmi";
	public static final String pathRDFTTL = "examples/com.emf4sw.rdf.examples/src/family.model.ttl";
	public static final String pathConvertedModelInstance = "examples/com.emf4sw.rdf.examples/src/family.ttl.xmi";
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		// Register Factroy XMIResourceFactoryImpl to read ecore, xmi files
		
		EPackage.Registry.INSTANCE.put(family.FamilyPackage.eNS_URI, family.FamilyPackage.eINSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		// Register Factroy TTLResourceFactory to read turtle files
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		// Here we use Models class from atl-commons to register models in a ResourceSet		
		Models.register(Models.resource(pathEcoreModel, true));
		
		RDF2Model r2m = new RDF2Model(Models.resource(pathEcoreModel, true));
		
		OWLOntologyManager owl2ttl = OWLManager.createOWLOntologyManager();
		InputStream owlInStream = new FileInputStream("examples/com.emf4sw.rdf.examples/src/family.model.owl");
		OWLOntology owlIn = owl2ttl.loadOntologyFromOntologyDocument(owlInStream);
		OutputStream ttlOutStream=new FileOutputStream(pathRDFTTL);
		owl2ttl.saveOntology(owlIn, new TurtleDocumentFormat(), ttlOutStream);
		ttlOutStream.close();
		
		
		Resource model = r2m.transform(Models.resource(pathRDFTTL, true));
		
		model.setURI(URI.createURI(pathConvertedModelInstance));
		try {
			model.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

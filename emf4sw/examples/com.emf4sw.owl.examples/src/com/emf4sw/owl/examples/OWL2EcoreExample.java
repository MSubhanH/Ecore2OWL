package com.emf4sw.owl.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import com.emf4sw.owl.resource.impl.TTLResourceFactory;
import com.emf4sw.owl.transform.owl2ecore.OWL2Ecore;

import CardinalityMapper.mapCardinality;

/**
 * OWL Example
 * 
 * This example shows how to transform an OWL Ontology into an Ecore Model.
 * 
 * @author guillaume hillairet
 *
 */
public class OWL2EcoreExample {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException, OWLOntologyCreationException, OWLOntologyStorageException {
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		OWLOntologyManager owl2ttl = OWLManager.createOWLOntologyManager();
		InputStream owlInStream = new FileInputStream("examples/com.emf4sw.owl.examples/src/Family.owl");
		OWLOntology owlIn = owl2ttl.loadOntologyFromOntologyDocument(owlInStream);
		OutputStream ttlOutStream=new FileOutputStream("examples/com.emf4sw.owl.examples/src/Family.ttl");
		owl2ttl.saveOntology(owlIn, new TurtleDocumentFormat(), ttlOutStream);
		ttlOutStream.close();

		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource owl = resourceSet.createResource(URI.createURI("examples/com.emf4sw.owl.examples/src/Family.ttl"));
		owl.load(null);
		
		Resource model = new OWL2Ecore().apply(owl);
		
		model.setURI(URI.createURI("examples/com.emf4sw.rdf.examples/src/Family.ecore"));
		model.save(null);
		
		mapCardinality newOWLToEcoreMapper = new mapCardinality("owl2ecore", "examples/com.emf4sw.rdf.examples/src/Family.ecore", "examples/com.emf4sw.owl.examples/src/Family.owl");
	}
}

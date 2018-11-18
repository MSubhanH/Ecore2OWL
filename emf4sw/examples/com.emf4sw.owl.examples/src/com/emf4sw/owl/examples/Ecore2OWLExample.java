package com.emf4sw.owl.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
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

import com.atl.common.models.Models;
import com.emf4sw.owl.resource.OWLFormats;
import com.emf4sw.owl.resource.impl.OWLXMLResourceFactory;
import com.emf4sw.owl.resource.impl.TTLResourceFactory;
import com.emf4sw.owl.transform.ecore2owl.Ecore2OWL;
import com.emf4sw.rdf.resource.RDFFormats;
import com.emf4sw.rdf.transform.Model2RDF;
import com.emf4sw.rdf.transform.RDFTransformation;

import CardinalityMapper.mapCardinality;

/**
 * OWL Example
 * 
 * This example shows how to convert a Ecore Model into an OWL Model, and how to save this latter 
 * in OWL/XML and Turtle. 
 * 
 * @author guillaume hillairet
 *
 */
public class Ecore2OWLExample {
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException, OWLOntologyCreationException, OWLOntologyStorageException {
				
//		ResourceSet resSet = new ResourceSetImpl();
		        
		//EPackage.Registry.INSTANCE.put(person.PersonPackage.eNS_URI, person.PersonPackage.eINSTANCE);
    
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("owl", new OWLXMLResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource meta = resourceSet.createResource(URI.createURI("examples/com.emf4sw.owl.examples/src/Family.ecore"));
		meta.load(null);
				
		Resource owl = new Ecore2OWL(OWLFormats.XMI).apply(meta);
		owl.setURI(URI.createURI("examples/com.emf4sw.owl.examples/src/Family.owl.xmi"));
		owl.save(null);
		
		Resource owlxml = new Ecore2OWL(OWLFormats.OWL).apply(meta);
		owlxml.setURI(URI.createURI("examples/com.emf4sw.owl.examples/src/Family.owl"));
		owlxml.save(null);
		
		Resource owlttl = new Ecore2OWL(OWLFormats.TURTLE).apply(meta);
		owlttl.setURI(URI.createURI("examples/com.emf4sw.owl.examples/src/Family.ttl"));
		owlttl.save(null); 
		
		mapCardinality newEcoreToOWLMapper = new mapCardinality("ecore2owl", "examples/com.emf4sw.owl.examples/src/Family.ecore", "examples/com.emf4sw.owl.examples/src/Family.owl");
	
//		OWLOntologyManager owl2owl = OWLManager.createOWLOntologyManager();
//		InputStream owlInStream = new FileInputStream("examples/com.emf4sw.owl.examples/src/Family.owl");
//		OWLOntology owlIn = owl2owl.loadOntologyFromOntologyDocument(owlInStream);
//		OutputStream owlOutStream=new FileOutputStream("examples/com.emf4sw.owl.examples/src/Family.owl");
//		owl2owl.saveOntology(owlIn, new RDFXMLDocumentFormat(), owlOutStream);
//		owlOutStream.close();
//	
//		OWLOntologyManager owl2ttl = OWLManager.createOWLOntologyManager();
//		OutputStream ttlOutStream=new FileOutputStream("examples/com.emf4sw.owl.examples/src/Family.ttl");
//		owl2ttl.saveOntology(owlIn, new TurtleDocumentFormat(), ttlOutStream);
//		ttlOutStream.close();
	
	}
	
}

package com.emf4sw.rdf.examples;

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
import com.emf4sw.rdf.resource.RDFFormats;
import com.emf4sw.rdf.resource.impl.TTLResourceFactory;
import com.emf4sw.rdf.transform.Model2RDF;
import com.emf4sw.rdf.transform.RDFTransformation;

import CardinalityMapper.mapCardinality;

/**
 * 
 * 
 * @author guillaume hillairet
 *
 */
public class EcoreModelInRdf {

	public static final String pathEcoreModel = "examples/com.emf4sw.rdf.examples/src/Family.ecore";
	public static final String pathEcoreModelInstance = "examples/com.emf4sw.rdf.examples/src/model.family.xmi";
	public static final String pathRDFTTL = "examples/com.emf4sw.rdf.examples/src/family.model.ttl";
	
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, TransformerException, IOException {
		// Register Factroy XMIResourceFactoryImpl to read ecore, xmi files
			
		EPackage.Registry.INSTANCE.put(person.PersonPackage.eNS_URI, person.PersonPackage.eINSTANCE);
		EPackage.Registry.INSTANCE.put(family.FamilyPackage.eNS_URI, family.FamilyPackage.eINSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		// Register Factroy TTLResourceFactory to read turtle files
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		// Here we use Models class from atl-commons to register models in a ResourceSet		
		Models.register(Models.resource(pathEcoreModel, true));
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(RDFTransformation.OPTION_RDF_FORMAT, RDFFormats.TURTLE_FORMAT);
		
		Model2RDF model2rdf = new Model2RDF();
		Resource rdfResult = model2rdf.transform(Models.resource(pathEcoreModelInstance, true), options);
		
		rdfResult.setURI(URI.createURI(pathRDFTTL));
		
		rdfResult.save(null);
			
		OWLOntologyManager ttl2owl = OWLManager.createOWLOntologyManager();
		InputStream ttlInStream = new FileInputStream("examples/com.emf4sw.rdf.examples/src/family.model.ttl");
		OWLOntology ttlIn = ttl2owl.loadOntologyFromOntologyDocument(ttlInStream);
		OutputStream owlOutStream=new FileOutputStream("examples/com.emf4sw.rdf.examples/src/family.model.owl");
		ttl2owl.saveOntology(ttlIn, new RDFXMLDocumentFormat(), owlOutStream);
		owlOutStream.close();
		
		mapCardinality newOWLIntanceUpdater = new mapCardinality("updateOWLInstance", "examples\\com.emf4sw.owl.examples\\src\\Family.owl", "examples/com.emf4sw.rdf.examples/src/family.model.owl");

			

	}
	
}

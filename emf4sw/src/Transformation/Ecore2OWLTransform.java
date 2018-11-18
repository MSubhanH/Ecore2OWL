package Transformation;

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

import com.emf4sw.owl.resource.OWLFormats;
import com.emf4sw.owl.resource.impl.OWLXMLResourceFactory;
import com.emf4sw.owl.resource.impl.TTLResourceFactory;
import com.emf4sw.owl.transform.ecore2owl.Ecore2OWL;


import CardinalityMapper.mapCardinality;

public class Ecore2OWLTransform {
	
	public String pathEcoreFolder;
	public String ecoreModelName;

	public String pathOwlFolder;
	public String owlOntologyName;
	
	public Ecore2OWLTransform(String EcoreModelFolder, String EcoreModelName, String OWLOntologyFolder, String OWLOntologyName) throws  IOException, ParserConfigurationException, SAXException, TransformerException {
		
		pathEcoreFolder = EcoreModelFolder;
		ecoreModelName = EcoreModelName;
		
		pathOwlFolder = OWLOntologyFolder;
		owlOntologyName = OWLOntologyName;
		
		try {
			TransformEcore2OWL();
		} catch (OWLOntologyStorageException | OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void TransformEcore2OWL() throws OWLOntologyStorageException, IOException, ParserConfigurationException, SAXException, TransformerException, OWLOntologyCreationException {
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("owl", new OWLXMLResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		
		Resource meta = resourceSet.createResource(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"));
		meta.load(null);
			
		Resource owl = new Ecore2OWL(OWLFormats.XMI).apply(meta);
		owl.setURI(URI.createFileURI(pathOwlFolder + owlOntologyName + ".owl.xmi"));
		owl.save(null);
		
		Resource owlxml = new Ecore2OWL(OWLFormats.OWL).apply(meta);
		owlxml.setURI(URI.createFileURI(pathOwlFolder + owlOntologyName + ".owl"));
		owlxml.save(null);
		
		Resource owlttl = new Ecore2OWL(OWLFormats.TURTLE).apply(meta);
		owlttl.setURI(URI.createFileURI(pathOwlFolder + owlOntologyName + ".ttl"));
		owlttl.save(null); 
		
		mapCardinality newEcoreToOWLMapper = new mapCardinality("ecore2owl", pathEcoreFolder + ecoreModelName + ".ecore", pathOwlFolder + owlOntologyName + ".owl");
	
	}
	
}


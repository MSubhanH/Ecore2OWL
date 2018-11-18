package Transformation;

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
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import com.emf4sw.owl.resource.impl.TTLResourceFactory;
import com.emf4sw.owl.transform.owl2ecore.OWL2Ecore;

import CardinalityMapper.mapCardinality;

public class OWL2EcoreTransform {

	public String pathEcoreFolder;
	public String ecoreModelName;

	public String pathOwlFolder;
	public String owlOntologyName;
	
	public OWL2EcoreTransform(String EcoreModelFolder, String EcoreModelName, String OWLOntologyFolder, String OWLOntologyName) throws  IOException, ParserConfigurationException, SAXException, TransformerException,  org.eclipse.m2m.atl.engine.emfvm.VMException, java.lang.ArrayStoreException {
		
		pathEcoreFolder = EcoreModelFolder;
		ecoreModelName = EcoreModelName;
		
		pathOwlFolder = OWLOntologyFolder;
		owlOntologyName = OWLOntologyName;

		try {
			TransformOWL2Ecore();
		} catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void TransformOWL2Ecore() throws IOException, ParserConfigurationException, SAXException, TransformerException, OWLOntologyCreationException, OWLOntologyStorageException{
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		OWLOntologyManager owl2ttl = OWLManager.createOWLOntologyManager();
		InputStream owlInStream = new FileInputStream(pathOwlFolder + owlOntologyName + ".owl");
		OWLOntology owlIn = owl2ttl.loadOntologyFromOntologyDocument(owlInStream);
		OutputStream ttlOutStream=new FileOutputStream(pathOwlFolder + owlOntologyName + ".ttl");
		owl2ttl.saveOntology(owlIn, new TurtleDocumentFormat(), ttlOutStream);
		ttlOutStream.close();
		
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource owl = resourceSet.createResource(URI.createFileURI(pathOwlFolder + owlOntologyName + ".ttl"));
		owl.load(null);
		
		Resource model = new OWL2Ecore().apply(owl);
		
		model.setURI(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"));
		model.save(null);
		
		mapCardinality newOWLToEcoreMapper = new mapCardinality("owl2ecore", pathEcoreFolder + ecoreModelName + ".ecore", pathOwlFolder + owlOntologyName + ".owl");
		
	}
	
}

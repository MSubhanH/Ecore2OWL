package Transformation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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
import org.xml.sax.SAXException;

import com.atl.common.models.Models;
import com.emf4sw.rdf.resource.impl.TTLResourceFactory;
import com.emf4sw.rdf.transform.RDF2Model;

public class OWLIns2EcoreInsTransform {
	
	public String pathEcoreFolder;
	public String ecoreModelName;

	public String pathEcoreInsFolder;
	public String ecoreInsName;
	
	public String pathOwlInsFolder;
	public String owlInsName;
	
	public OWLIns2EcoreInsTransform(String EcoreModelFolder, String EcoreModelName, String EcoreInsFolder, String EcoreInsName, String OWLInsFolder, String OWLInsName)  throws  IOException, ParserConfigurationException, SAXException, TransformerException   {
		
		pathEcoreFolder = EcoreModelFolder;
		ecoreModelName = EcoreModelName;
		
		pathEcoreInsFolder = EcoreInsFolder;
		ecoreInsName = EcoreInsName;
		
		pathOwlInsFolder = OWLInsFolder;
		owlInsName = OWLInsName;
		
		try {
			TransformOWLIns2EcoreIns();
		} catch (OWLOntologyCreationException | OWLOntologyStorageException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public void TransformOWLIns2EcoreIns() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
		
		EPackage.Registry.INSTANCE.put(family.FamilyPackage.eNS_URI, family.FamilyPackage.eINSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		// Register Factroy TTLResourceFactory to read turtle files
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		// Here we use Models class from atl-commons to register models in a ResourceSet		
		Models.register(Models.resource(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"), true));
		
		RDF2Model r2m = new RDF2Model(Models.resource(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"), true));
		
		OWLOntologyManager owl2ttl = OWLManager.createOWLOntologyManager();
		InputStream owlInStream = new FileInputStream(pathOwlInsFolder + owlInsName + ".owl");
		OWLOntology owlIn = owl2ttl.loadOntologyFromOntologyDocument(owlInStream);
		OutputStream ttlOutStream=new FileOutputStream(pathOwlInsFolder + owlInsName + ".ttl");
		owl2ttl.saveOntology(owlIn, new TurtleDocumentFormat(), ttlOutStream);
		ttlOutStream.close();
		
		Resource model = r2m.transform(Models.resource(URI.createFileURI(pathOwlInsFolder + owlInsName + ".ttl"), true));
		
		model.setURI(URI.createFileURI(pathEcoreInsFolder + ecoreInsName + ".xmi"));
		
		try {
			model.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}

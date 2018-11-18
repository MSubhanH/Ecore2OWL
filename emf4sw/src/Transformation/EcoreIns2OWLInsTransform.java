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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import com.atl.common.models.Models;
import com.emf4sw.rdf.resource.RDFFormats;
import com.emf4sw.rdf.resource.impl.TTLResourceFactory;
import com.emf4sw.rdf.transform.Model2RDF;
import com.emf4sw.rdf.transform.RDF2ModelGen;
import com.emf4sw.rdf.transform.RDFTransformation;

import CardinalityMapper.mapCardinality;

public class EcoreIns2OWLInsTransform {
	
	public String pathEcoreFolder;
	public String ecoreModelName;

	public String pathOwlFolder;
	public String owlOntologyName;
	
	public String pathEcoreInsFolder;
	public String ecoreInsName;
	
	public String pathOwlInsFolder;
	public String owlInsName;
	
	public EcoreIns2OWLInsTransform(String EcoreModelFolder, String EcoreModelName, String OWLOntology, String OWLOntologyName, String EcoreInsFolder, String EcoreInsName, String OWLInsFolder, String OWLInsName) throws  IOException, ParserConfigurationException, SAXException, TransformerException {
		
		pathEcoreFolder = EcoreModelFolder;
		ecoreModelName = EcoreModelName;
		
		pathEcoreInsFolder = EcoreInsFolder;
		ecoreInsName = EcoreInsName;
		
		pathOwlFolder = OWLOntology;
		owlOntologyName = OWLOntologyName;		
		
		pathOwlInsFolder = OWLInsFolder;
		owlInsName = OWLInsName;
		
		try {
			TransformEcoreIns2OWLIns();
		} catch (OWLOntologyCreationException | OWLOntologyStorageException | ParserConfigurationException
				| SAXException | TransformerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		
	}
	
	public void TransformEcoreIns2OWLIns() throws OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, TransformerException, IOException {
		
		EPackage.Registry.INSTANCE.put(family.FamilyPackage.eNS_URI, family.FamilyPackage.eINSTANCE);
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());
		
		// Register Factroy TTLResourceFactory to read turtle files
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ttl", new TTLResourceFactory());
		
		// Here we use Models class from atl-commons to register models in a ResourceSet		
		Models.register(Models.resource(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"), true));
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put(RDFTransformation.OPTION_RDF_FORMAT, RDFFormats.TURTLE_FORMAT);
		
		Model2RDF model2rdf = new Model2RDF();
		Resource rdfResult = model2rdf.transform(Models.resource(URI.createFileURI(pathEcoreInsFolder + ecoreInsName + ".xmi"), true), options);
		
		rdfResult.setURI(URI.createFileURI(pathOwlInsFolder + owlInsName + ".ttl"));
		

		rdfResult.save(null);
			
		OWLOntologyManager ttl2owl = OWLManager.createOWLOntologyManager();
		InputStream ttlInStream = new FileInputStream(pathOwlInsFolder + owlInsName + ".ttl");
		OWLOntology ttlIn = ttl2owl.loadOntologyFromOntologyDocument(ttlInStream);
		OutputStream owlOutStream=new FileOutputStream(pathOwlInsFolder + owlInsName + ".owl");
		ttl2owl.saveOntology(ttlIn, new RDFXMLDocumentFormat(), owlOutStream);
		owlOutStream.close();
			
		mapCardinality newOWLIntanceUpdater = new mapCardinality("updateOWLInstance", pathOwlFolder + owlOntologyName + ".owl", pathOwlInsFolder + owlInsName + ".owl");
	
		RDF2ModelGen generator = new RDF2ModelGen(Models.resource(URI.createFileURI(pathEcoreFolder + ecoreModelName + ".ecore"), true));
		Resource atl = generator.getResource();
			
		atl.setURI(URI.createFileURI(pathOwlInsFolder + "rdf2" + ecoreModelName + ".rdf"));
		
		atl.save(null);
						
				
	}
	
}

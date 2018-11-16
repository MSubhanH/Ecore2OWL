package CardinalityMapper;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UpdateOWLInstance {
	
	public String pathOWLOntology;
	public String pathOWLOntologyInstance;
	
	public OWLDatatypePropertyProperties[] OWLDatatypeproperties ;
	public int countOfOWLDatatypeProperties;
	
	public OWLObjectPropertyProperties[] OWLObjectproperties ;
	public int countOfOWLObjectProperties;
	
	public OWLAnnotation[] OWLAnnotations;
	public int countOfOWLAnnotations;
	
	public UpdateOWLInstance(String OWLOntology, String OWLOntologyInstance) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		pathOWLOntology = OWLOntology;
		pathOWLOntologyInstance = OWLOntologyInstance;
		
		extractCardinalitiesFromOWL() ;
		
	}
	
	public void extractCardinalitiesFromOWL() throws ParserConfigurationException, SAXException, IOException, TransformerException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	// Create instance of document that will store .xmi model file 
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document owlOntology = docBuilder.parse(pathOWLOntology);

		NodeList listOfNodes = owlOntology.getElementsByTagName("owl:DatatypeProperty"); // Store list of all nodes in that exist in model file. 
		
		NodeList childrenOfNode = listOfNodes;
		NamedNodeMap attrOfNode = null;
		String memberTypeVal = "null";
		
		countOfOWLDatatypeProperties = 0;

		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("rdf:about") != null) 
			{
				memberTypeVal =  attrOfNode.getNamedItem("rdf:about").getNodeValue();
				
				countOfOWLDatatypeProperties++;
			}
			
			attrOfNode = null;
			
		}
		
		
		OWLDatatypeproperties = new OWLDatatypePropertyProperties[countOfOWLDatatypeProperties];
		int t = 0;
		
		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("rdf:about") != null) 
			{					
				OWLDatatypeproperties[t] = new OWLDatatypePropertyProperties();
				
				OWLDatatypeproperties[t].index = i;
				OWLDatatypeproperties[t].rdfAbout = attrOfNode.getNamedItem("rdf:about").getNodeValue();		

				childrenOfNode = childrenOfNode.item(i).getChildNodes();
				
				for(int q = 0; q < childrenOfNode.getLength(); q++) {
					
					attrOfNode = childrenOfNode.item(q).getAttributes();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:domain") == 0) {
						OWLDatatypeproperties[t].Domain = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					}
										
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:range") == 0) {
						OWLDatatypeproperties[t].Range = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					}
				}
					
				t++;
			}
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
	
		}
			
		listOfNodes = owlOntology.getElementsByTagName("owl:ObjectProperty"); 

		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		countOfOWLObjectProperties = 0;
		boolean isObjectProperty = false;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			childrenOfNode = childrenOfNode.item(i).getChildNodes();
						
			for(int k = 0; k < childrenOfNode.getLength(); k++) {
				
				if(childrenOfNode.item(k).getNodeName().compareTo("rdfs:range") == 0 || childrenOfNode.item(k).getNodeName().compareTo("rdfs:domain") == 0)
					isObjectProperty = true;
			}
						
			if(isObjectProperty) 
				countOfOWLObjectProperties++;		
			
			isObjectProperty = false;

			childrenOfNode = listOfNodes;
			attrOfNode = null;
		}

		OWLObjectproperties = new OWLObjectPropertyProperties[countOfOWLObjectProperties];
		int u = 0;
		
		isObjectProperty = false;

		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			childrenOfNode = childrenOfNode.item(i).getChildNodes();
			
			for(int k = 0; k < childrenOfNode.getLength(); k++) {
			
				if(childrenOfNode.item(k).getNodeName().compareTo("rdfs:domain") == 0 || childrenOfNode.item(k).getNodeName().compareTo("rdfs:range") == 0) {
					isObjectProperty = true;
					attrOfNode = childrenOfNode.item(k).getAttributes();				
				}
					
			}
			
			
			if(isObjectProperty) {				

				OWLObjectproperties[u] = new OWLObjectPropertyProperties();
				
				OWLObjectproperties[u].index = i;
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:about") != null) 
					OWLObjectproperties[u].name = listOfNodes.item(i).getAttributes().getNamedItem("rdf:about").getNodeValue();
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID") != null) {
					OWLObjectproperties[u].name = listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue();
				}
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:comment") != null) {
					OWLObjectproperties[u].name = listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue();
				}
							
				OWLObjectproperties[u].Domain = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
				OWLObjectproperties[u].Range = attrOfNode.getNamedItem("rdf:resource").getNodeValue();	
				
				u++;
			}
			
			isObjectProperty = false;
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
		}

		//for(int i = 0; i < countOfOWLDatatypeProperties; i++) {
		//	System.out.println(countOfOWLDatatypeProperties);
		//}
		
		updateInstance();
	}
	
	public void updateInstance() throws TransformerException, ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	// Create instance of document that will store .xmi model file 
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document owlOntology = docBuilder.parse(pathOWLOntologyInstance);

		NodeList listOfNodes = owlOntology.getElementsByTagName("AnnotationProperty"); // Store list of all nodes in that exist in model file. 
		
		NodeList childrenOfNode = listOfNodes;
		NamedNodeMap attrOfNode = null;
		
		countOfOWLAnnotations = 0;

		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("rdf:about") != null) 	
				countOfOWLAnnotations++;
		
			attrOfNode = null;
			
		}
	
		OWLAnnotations = new OWLAnnotation[countOfOWLAnnotations];
		int t = 0;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("rdf:about") != null)
			{
				OWLAnnotations[t] = new OWLAnnotation();

				OWLAnnotations[t].name = attrOfNode.getNamedItem("rdf:about").getNodeValue();
				OWLAnnotations[t].name = OWLAnnotations[t].name.substring(OWLAnnotations[t].name.indexOf("#") + 1, OWLAnnotations[t].name.length());			
						
				OWLAnnotations[t].isToBeRemoved = false;
				
				for(int p = 0; p < countOfOWLDatatypeProperties; p++) {
					
					String OWLDatapropertyname = OWLDatatypeproperties[p].rdfAbout.substring(OWLDatatypeproperties[p].rdfAbout.indexOf("#") + 1, OWLDatatypeproperties[p].rdfAbout.length());
					
					if(OWLAnnotations[t].name.compareTo(OWLDatapropertyname) == 0)
						OWLAnnotations[t].isToBeRemoved = true;
										
				}
				
				for(int p = 0; p < countOfOWLObjectProperties; p++) {

					String OWLObjectPropertyName  = OWLObjectproperties[p].name.substring(OWLObjectproperties[p].name.indexOf("#") + 1, OWLObjectproperties[p].name.length());
							
					if(OWLAnnotations[t].name.compareTo(OWLObjectPropertyName) == 0) {
						OWLAnnotations[t].isToBeRemoved = true;
					}
						
				}
			}
			
			t++;
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
			
		}
		
		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		t = 0;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("rdf:about") != null) {

				String annotName = attrOfNode.getNamedItem("rdf:about").getNodeValue();
				annotName = annotName.substring(annotName.indexOf("#") + 1, annotName.length());
				
				for(int p = 0; p < countOfOWLAnnotations; p++) {
						
					if(annotName.compareTo(OWLAnnotations[p].name) == 0 && OWLAnnotations[p].isToBeRemoved == true) {
					
						childrenOfNode.item(i).getParentNode().removeChild(childrenOfNode.item(i));
						i--;
					}
				}			
			}
						
			t++;
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
			
		}
		
//		for(int i = 0; i < countOfOWLAnnotations; i++) {
//		
//		System.out.println(OWLAnnots[i].name + "  " + OWLAnnots[i].isToBeRemoved);
//	}
	

		listOfNodes = owlOntology.getElementsByTagName("rdf:RDF"); 
			
		Node rdfDoc = listOfNodes.item(0);
		
		for(int p = 0; p < countOfOWLDatatypeProperties; p++) {
			
			Element DataPropertyNode = owlOntology.createElement("owl:DatatypeProperty");
			DataPropertyNode.setAttribute("rdf:about", OWLDatatypeproperties[p].rdfAbout);

			Element domainOfDataProperty = owlOntology.createElement("rdfs:domain");
			domainOfDataProperty.setAttribute("rdf:resource",OWLDatatypeproperties[p].Domain);
			
			Element rangeOfDataProperty = owlOntology.createElement("rdfs:range");
			rangeOfDataProperty.setAttribute("rdf:resource", OWLDatatypeproperties[p].Range);
			
			DataPropertyNode.appendChild(domainOfDataProperty);
			DataPropertyNode.appendChild(rangeOfDataProperty);
			
			rdfDoc.appendChild(DataPropertyNode);
								
		}
		
		
		for(int p = 0; p < countOfOWLObjectProperties; p++) {
			
			Element ObjectPropertyNode = owlOntology.createElement("owl:ObjectProperty");
			ObjectPropertyNode.setAttribute("rdf:about", OWLObjectproperties[p].name);

			Element domainOfObjectProperty = owlOntology.createElement("rdfs:domain");
			domainOfObjectProperty.setAttribute("rdf:resource", OWLObjectproperties[p].Domain);
			
			Element rangeOfObjectProperty = owlOntology.createElement("rdfs:range");
			rangeOfObjectProperty.setAttribute("rdf:resource", OWLObjectproperties[p].Range);
							
			ObjectPropertyNode.appendChild(domainOfObjectProperty);
			ObjectPropertyNode.appendChild(rangeOfObjectProperty);
					
			rdfDoc.appendChild(ObjectPropertyNode);
								
		}
		
		owlOntology.getDocumentElement().normalize();
	    
		TransformerFactory transformerFactory = TransformerFactory.newInstance();	
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(owlOntology);			// Create a copy of the .xmi model file
        StreamResult result = new StreamResult(new File(pathOWLOntologyInstance));	// Set file path for the updated .xmi model file in result
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);			// Save the updated .xmi model file in result.
        System.out.println("XML file updated successfully");
	
		
	}
	


}

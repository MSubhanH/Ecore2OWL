package CardinalityMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MapEcore2OWL {

	public String pathEcoreModel;
	public String pathOWLOntology;

	public EAttributeProperties[] EAttributes;
	public int countOfEAttributes;
	
	public EReferenceProperties[] EReferences;
	public int countOfEReferences;
	
	public OWLDatatypePropertyProperties[] OWLDatatypeproperties ;
	public int countOfOWLDatatypeProperties;
	
	public OWLObjectPropertyProperties[] OWLObjectproperties; 
	public int countOfOWLObjectProperties;
	
	public MapEcore2OWL(String EcoreModel, String OWLOntology) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		pathEcoreModel = EcoreModel;
		pathOWLOntology = OWLOntology;
		
		extractCardinalitiesFromEcore() ;
		
		
	}
	
	public void extractCardinalitiesFromEcore() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	// Create instance of document that will store .xmi model file 
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document ecoreModel = docBuilder.parse(pathEcoreModel);
		
		NodeList listOfNodes = ecoreModel.getElementsByTagName("eStructuralFeatures"); // Store list of all nodes in that exist in model file. 
		NodeList childrenOfNode = listOfNodes;
		NamedNodeMap attrOfNode = null;		
			
		String memberTypeVal = "null";
		
		countOfEAttributes = 0;
		countOfEReferences = 0;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("xsi:type") != null) 
				memberTypeVal = attrOfNode.getNamedItem("xsi:type").getNodeValue().substring(6, attrOfNode.getNamedItem("xsi:type").getNodeValue().length());
			
			if(memberTypeVal.compareTo("EAttribute") == 0) 
				countOfEAttributes++;
			
			if(memberTypeVal.compareTo("EReference") == 0) 
				countOfEReferences++;
			
		}
			
		EAttributes = new EAttributeProperties[countOfEAttributes];
		int c = 0;
		
		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("xsi:type") != null) 
				memberTypeVal = attrOfNode.getNamedItem("xsi:type").getNodeValue().substring(6, attrOfNode.getNamedItem("xsi:type").getNodeValue().length());
						
			if(memberTypeVal.compareTo("EAttribute") == 0) 
			{
				EAttributes[c] = new EAttributeProperties();
				
				EAttributes[c].index = i;
				
				if(attrOfNode.getNamedItem("changeable") != null) 
					EAttributes[c].Changeable = attrOfNode.getNamedItem("changeable").getNodeValue();
				
				if(attrOfNode.getNamedItem("defaultValueLiteral") != null) 
					 EAttributes[c].Default_Value_Literal = attrOfNode.getNamedItem("defaultValueLiteral").getNodeValue();
				
				if(attrOfNode.getNamedItem("eType") != null) {
					EAttributes[c].EType = attrOfNode.getNamedItem("eType").getNodeValue();
					EAttributes[c].EType = EAttributes[c].EType.substring(56, EAttributes[c].EType.length());
				}
					 
				
				if(attrOfNode.getNamedItem("iD") != null) 
					 EAttributes[c].ID = attrOfNode.getNamedItem("iD").getNodeValue();
				
				if(attrOfNode.getNamedItem("lowerBound") != null) 
					 EAttributes[c].Lower_Bound = attrOfNode.getNamedItem("lowerBound").getNodeValue();
				
				if(attrOfNode.getNamedItem("name") != null) 
					 EAttributes[c].Name = attrOfNode.getNamedItem("name").getNodeValue();
				
				if(attrOfNode.getNamedItem("ordered") != null) 
					 EAttributes[c].Ordered = attrOfNode.getNamedItem("ordered").getNodeValue();
			
				if(attrOfNode.getNamedItem("transient") != null) 
					 EAttributes[c].Transient = attrOfNode.getNamedItem("transient").getNodeValue();
				
				if(attrOfNode.getNamedItem("unique") != null) 
					 EAttributes[c].Unique = attrOfNode.getNamedItem("unique").getNodeValue();
				
				if(attrOfNode.getNamedItem("unsettable") != null) 
					 EAttributes[c].Unsettable = attrOfNode.getNamedItem("unsettable").getNodeValue();
				
				if(attrOfNode.getNamedItem("upperBound") != null) 
					 EAttributes[c].Upper_Bound = attrOfNode.getNamedItem("upperBound").getNodeValue();
				
				if(attrOfNode.getNamedItem("volatile") != null) 
					 EAttributes[c].Volatile = attrOfNode.getNamedItem("volatile").getNodeValue();
				
				c++;
				
			}
			
		}

		childrenOfNode = listOfNodes;
		attrOfNode = null;	
		
		EReferences = new EReferenceProperties[countOfEReferences];
		int p = 0;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = listOfNodes.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("xsi:type") != null) 
				memberTypeVal = attrOfNode.getNamedItem("xsi:type").getNodeValue().substring(6, attrOfNode.getNamedItem("xsi:type").getNodeValue().length());
			
			if(memberTypeVal.compareTo("EReference") == 0) 
			{
				EReferences[p] = new EReferenceProperties();
				
				EReferences[p].index = i;
					
				if(attrOfNode.getNamedItem("eType") != null) {
					EReferences[p].EType = attrOfNode.getNamedItem("eType").getNodeValue();
					EReferences[p].EType = EReferences[p].EType.substring(3, EReferences[p].EType.length());
				}
					 
				if(attrOfNode.getNamedItem("lowerBound") != null) 
					EReferences[p].Lower_Bound = attrOfNode.getNamedItem("lowerBound").getNodeValue();

				if(attrOfNode.getNamedItem("containment") != null) 
					EReferences[p].Containment = attrOfNode.getNamedItem("containment").getNodeValue();
				
				if(attrOfNode.getNamedItem("name") != null) 
					EReferences[p].Name = attrOfNode.getNamedItem("name").getNodeValue();
					
				if(attrOfNode.getNamedItem("upperBound") != null) 
					EReferences[p].Upper_Bound = attrOfNode.getNamedItem("upperBound").getNodeValue();
								
				p++;
				
			}
			
		}		
			
	//	for(int i = 0; i < countOfEAttributes; i++) {
		
	//	System.out.println(EAttributes[i].Name + "   " + EAttributes[i].EType);
	//	System.out.println(i+1 + "   " + EAttributes[i].Default_Value_Literal);
	//	System.out.println(i+1 + "   " + EAttributes[i].EType);
	//	System.out.println(i+1 + "   " + EAttributes[i].ID);
	//	System.out.println(" ");
	//}
		
	//	for(int i = 0; i < countOfEReferences; i++) {
			
	//		System.out.println(i+1 + "   " + EReferences[i].Name);
	//		System.out.println(i+1 + "   " + EReferences[i].EType);
	//		System.out.println(i+1 + "   " + EReferences[i].Lower_Bound);
	//		System.out.println(i+1 + "   " + EReferences[i].Upper_Bound);
	//		System.out.println(EReferences[i].Name + "   " + EReferences[i].Containment);
	//		System.out.println(" ");
	//	}
	
		mapCardinalitiesEcoreToOWL();

	}
	
	public void mapCardinalitiesEcoreToOWL() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document owlOntology = docBuilder.parse(pathOWLOntology);
		
		Map<String, String> datatypeMapping = createDatatypeMappingTableEcoreToOWL();

		String memberTypeVal = "null";
		
		countOfOWLDatatypeProperties = 0;
		countOfOWLObjectProperties = 0;
	
		
		NodeList listOfNodes = owlOntology.getElementsByTagName("owl:DatatypeProperty"); 
		NodeList childrenOfNode = listOfNodes;
		NamedNodeMap attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			//childrenOfNode = childrenOfNode.item(i).getChildNodes();
		
			if(attrOfNode.getNamedItem("rdf:about") != null) 
			{
				memberTypeVal = attrOfNode.getNamedItem("rdf:about").getNodeValue();
				countOfOWLDatatypeProperties++;
			}
			
		}
		
		OWLDatatypeproperties = new OWLDatatypePropertyProperties[countOfOWLDatatypeProperties];
		int t = 0;
	
		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
					
			if(attrOfNode.getNamedItem("rdf:about") != null) 
			{	
				Node OWLDatatypepropertyNode = listOfNodes.item(i);
				
				OWLDatatypeproperties[t] = new OWLDatatypePropertyProperties();
				
				OWLDatatypeproperties[t].index = i;
				OWLDatatypeproperties[t].rdfAbout = attrOfNode.getNamedItem("rdf:about").getNodeValue();
				OWLDatatypeproperties[t].rdfAbout = OWLDatatypeproperties[t].rdfAbout.substring(memberTypeVal.indexOf('#') + 1, OWLDatatypeproperties[t].rdfAbout.length());
							
				childrenOfNode = childrenOfNode.item(i).getChildNodes();
				
				for(int q = 0; q < childrenOfNode.getLength(); q++) {
					
					attrOfNode = childrenOfNode.item(q).getAttributes();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:domain") == 0) {
						if(attrOfNode.getNamedItem("rdf:resource") != null)
							OWLDatatypeproperties[t].Domain = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					}
					
				}
				
				for(int r = 0; r < countOfEAttributes; r++) {
					
					if(OWLDatatypeproperties[t].rdfAbout.compareTo(EAttributes[r].Name) == 0 )
						OWLDatatypeproperties[t].Range = datatypeMapping.get(EAttributes[r].EType);
			
				} 
								
				System.out.println(OWLDatatypeproperties[t].Range);
				
				Element rangeNode = owlOntology.createElement("rdfs:range");
				Attr rangeNodeResourseAttr = owlOntology.createAttribute("rdf:resource");
				
				rangeNodeResourseAttr.setNodeValue(OWLDatatypeproperties[t].Range);
				rangeNode.setAttributeNode(rangeNodeResourseAttr);
				
				OWLDatatypepropertyNode.appendChild(rangeNode);	
			}
	
			childrenOfNode = listOfNodes;
			attrOfNode = null;
		}

		
		listOfNodes = owlOntology.getElementsByTagName("owl:ObjectProperty"); 
		
		childrenOfNode = listOfNodes;
		attrOfNode = null;

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
			
		}
		
		/////// Stopped here
					
		OWLObjectproperties = new OWLObjectPropertyProperties[countOfOWLObjectProperties];
		int u = 0;
		isObjectProperty = false;
		
		listOfNodes = owlOntology.getElementsByTagName("owl:ObjectProperty"); 

		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = childrenOfNode.item(i).getAttributes();
			childrenOfNode = listOfNodes.item(i).getChildNodes();
			
			for(int k = 0; k < childrenOfNode.getLength(); k++) {
			
				if(childrenOfNode.item(k).getNodeName().compareTo("rdfs:range") == 0 || childrenOfNode.item(k).getNodeName().compareTo("rdfs:domain") == 0)
					isObjectProperty = true;
			}
			
			
			if(isObjectProperty) {				

				Node OWLObjectpropertiesNode = listOfNodes.item(i);
				
				OWLObjectproperties[u] = new OWLObjectPropertyProperties();
				
				OWLObjectproperties[u].index = i;
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:about") != null) {
					OWLObjectproperties[u].name = attrOfNode.getNamedItem("rdf:about").getNodeValue();
					OWLObjectproperties[u].name = OWLObjectproperties[u].name.substring(1, OWLObjectproperties[u].name.length());
				}
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID") != null) 
					OWLObjectproperties[u].name = attrOfNode.getNamedItem("rdf:ID").getNodeValue();
				
				
				for(int r = 0; r < countOfEReferences; r++) {
					
					if(OWLObjectproperties[u].name.compareTo(EReferences[r].Name) == 0 ) 
						OWLObjectproperties[u].owlComment = EReferences[r].Containment;
					
				} 
							
				if(OWLObjectproperties[u].owlComment != null) {
					
					if(OWLObjectproperties[u].owlComment.compareTo("true") == 0) {
						
						Element commentNode = owlOntology.createElement("rdfs:comment");
				
						commentNode.appendChild(commentNode.getOwnerDocument().createTextNode("containment = true"));
						
						OWLObjectpropertiesNode.appendChild(commentNode);	
						
					}

				}

				
				u++;
			}
			
			isObjectProperty = false;
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
			
		}
					
	//	for(int i = 0; i < countOfOWLObjecttypetypeProperties; i++) {
			
	//		System.out.println(OWLObjectproperties[i].name + "  " + OWLObjectproperties[i].owlComment);
			
	//	}
				
		TransformerFactory transformerFactory = TransformerFactory.newInstance();	
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(owlOntology);			// Create a copy of the .xmi model file
        StreamResult result = new StreamResult(new File(pathOWLOntology));	// Set file path for the updated .xmi model file in result
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);			// Save the updated .xmi model file in result.
        System.out.println("XML file updated successfully");

		
	}

	public Map<String, String> createDatatypeMappingTableEcoreToOWL() {
		
		Map<String, String> datatypeMapping = new HashMap<>();
		
		datatypeMapping.put("EString", "http://www.w3.org/2001/XMLSchema#string");
		datatypeMapping.put("EInteger", "http://www.w3.org/2001/XMLSchema#int");
		datatypeMapping.put("EBoolean", "http://www.w3.org/2001/XMLSchema#boolean");
		datatypeMapping.put("EBooleanObject", "http://www.w3.org/2001/XMLSchema#boolean");
		datatypeMapping.put("EByte", "http://www.w3.org/2001/XMLSchema#byte");
		datatypeMapping.put("EByteArray", "http://www.w3.org/2001/XMLSchema#hexBinary");
		datatypeMapping.put("EByteObject", "http://www.w3.org/2001/XMLSchema#byte");
		datatypeMapping.put("EChar", "http://www.w3.org/2001/XMLSchema#string");
		datatypeMapping.put("ECharacterObject", "http://www.w3.org/2001/XMLSchema#string");
		datatypeMapping.put("EDate", "http://www.w3.org/2001/XMLSchema#date");
		datatypeMapping.put("EDouble", "http://www.w3.org/2001/XMLSchema#double");
		datatypeMapping.put("EDoubleObject", "http://www.w3.org/2001/XMLSchema#double");
		datatypeMapping.put("EFloat", "http://www.w3.org/2001/XMLSchema#float");
		datatypeMapping.put("EFloatObject", "http://www.w3.org/2001/XMLSchema#float");
		datatypeMapping.put("EInt", "http://www.w3.org/2001/XMLSchema#int");
		datatypeMapping.put("EIntegerObject", "http://www.w3.org/2001/XMLSchema#int");
		datatypeMapping.put("ELong", "http://www.w3.org/2001/XMLSchema#long");
		datatypeMapping.put("ELongObject", "http://www.w3.org/2001/XMLSchema#long");
		datatypeMapping.put("EShort", "http://www.w3.org/2001/XMLSchema#short");
		datatypeMapping.put("EShortObject", "http://www.w3.org/2001/XMLSchema#short");
		datatypeMapping.put("EBigDecimal", "http://www.w3.org/2001/XMLSchema#decimal");
		datatypeMapping.put("EBigInteger", "http://www.w3.org/2001/XMLSchema#integer");
		
		return datatypeMapping;
	}

}

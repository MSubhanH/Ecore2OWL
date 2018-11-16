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

public class MapOWL2Ecore {
	
	public String pathEcoreModel;
	public String pathOWLOntology;

	public EAttributeProperties[] EAttributes;
	public int countOfEAttributes;
	
	public EReferenceProperties[] EReferences;
	public int countOfEReferences;
	
	public OWLDatatypePropertyProperties[] OWLDatatypeproperties ;
	public int countOfOWLDatatypeProperties;
	
	public OWLObjectPropertyProperties[] OWLObjectproperties ;
	public int countOfOWLObjectProperties;
	
	public OWLRestriction[] OWLRestrictions;
	public int countOfOWLRestrictions;
	
	public MapOWL2Ecore(String EcoreModel, String OWLOntology) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		
		pathEcoreModel = EcoreModel;
		pathOWLOntology = OWLOntology;
		
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
				OWLDatatypeproperties[t].rdfAbout = OWLDatatypeproperties[t].rdfAbout.substring(memberTypeVal.indexOf('#') + 1, OWLDatatypeproperties[t].rdfAbout.length());
				
				childrenOfNode = childrenOfNode.item(i).getChildNodes();
				
				for(int q = 0; q < childrenOfNode.getLength(); q++) {
					
					attrOfNode = childrenOfNode.item(q).getAttributes();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:domain") == 0) {
						if(attrOfNode.getNamedItem("rdf:resource")  != null)
							OWLDatatypeproperties[t].Domain = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					}
										
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:range") == 0) {
						if(attrOfNode.getNamedItem("rdf:resource")  != null)
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
			
				if(childrenOfNode.item(k).getNodeName().compareTo("rdfs:range") == 0 || childrenOfNode.item(k).getNodeName().compareTo("rdfs:domain") == 0)
					isObjectProperty = true;
			}
			
			
			if(isObjectProperty) {				

				OWLObjectproperties[u] = new OWLObjectPropertyProperties();
				
				OWLObjectproperties[u].index = i;
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:about") != null) {
					OWLObjectproperties[u].name = listOfNodes.item(i).getAttributes().getNamedItem("rdf:about").getNodeValue();
					OWLObjectproperties[u].name = OWLObjectproperties[u].name.substring(OWLObjectproperties[u].name.indexOf("#") + 1, OWLObjectproperties[u].name.length());
				}
				
				if(listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID") != null) {
					OWLObjectproperties[u].name = listOfNodes.item(i).getAttributes().getNamedItem("rdf:ID").getNodeValue();
				}
							
				for(int q = 0; q < childrenOfNode.getLength(); q++) {
					
					attrOfNode = childrenOfNode.item(q).getAttributes();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:domain") == 0) 
						if(attrOfNode.getNamedItem("rdf:resource") != null)
							OWLObjectproperties[u].Domain = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:range") == 0) 
						if(attrOfNode.getNamedItem("rdf:resource") != null)
							OWLObjectproperties[u].Range = attrOfNode.getNamedItem("rdf:resource").getNodeValue();
					
					if(childrenOfNode.item(q).getNodeName().compareTo("rdfs:comment") == 0) 
						if(childrenOfNode.item(q) != null)
							OWLObjectproperties[u].owlComment = childrenOfNode.item(q).getTextContent();
				}
				
				u++;
			}
			
			isObjectProperty = false;
			
			childrenOfNode = listOfNodes;
			attrOfNode = null;
		}

		
		extractRestrictions();		
	}
	
	public void extractRestrictions() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	// Create instance of document that will store .xmi model file 
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document owlOntology = docBuilder.parse(pathOWLOntology);

		NodeList listOfNodes = owlOntology.getElementsByTagName("owl:Restriction"); // Store list of all nodes in that exist in model file. 
		NodeList childrenOfNode = listOfNodes;
		
		countOfOWLRestrictions = listOfNodes.getLength();
		
		OWLRestrictions = new OWLRestriction[countOfOWLRestrictions];
		int t = 0;
		
	 
		for(int k = 0; k < countOfOWLRestrictions; k++) {
			
			childrenOfNode = childrenOfNode.item(k).getChildNodes();
			
			OWLRestrictions[t] = new OWLRestriction();
			
			for(int i = 0; i < childrenOfNode.getLength(); i++) {
				
				if(childrenOfNode.item(i).getNodeName().compareTo("owl:onProperty") == 0) {
					
					Node onPropertyNode = childrenOfNode.item(i);
					
					childrenOfNode = childrenOfNode.item(i).getChildNodes();
					
					if(childrenOfNode.getLength() != 0) {
					
						for(int j = 0; j < childrenOfNode.getLength(); j++) {
							
							if(childrenOfNode.item(j).getNodeName().compareTo("owl:DatatypeProperty") == 0) {
								
								if(childrenOfNode.item(j).getAttributes().getNamedItem("rdf:ID").getNodeValue() != null)
									OWLRestrictions[t].onProperty = childrenOfNode.item(j).getAttributes().getNamedItem("rdf:ID").getNodeValue();
								
								else if(childrenOfNode.item(j).getAttributes().getNamedItem("rdf:resource").getNodeValue() != null) {
									OWLRestrictions[t].onProperty = childrenOfNode.item(j).getAttributes().getNamedItem("rdf:resource").getNodeValue();
									OWLRestrictions[t].onProperty = OWLRestrictions[t].onProperty.substring(OWLRestrictions[t].onProperty.indexOf("#") + 1, OWLRestrictions[t].onProperty.length());
								}
									
									
								OWLRestrictions[t].onPropertyType = "owl:DatatypeProperty";
							}	
							
							if(childrenOfNode.item(j).getNodeName().compareTo("owl:ObjectProperty") == 0) {
								
								if(childrenOfNode.item(j).getAttributes().getNamedItem("rdf:ID").getNodeValue() != null)
									OWLRestrictions[t].onProperty = childrenOfNode.item(j).getAttributes().getNamedItem("rdf:ID").getNodeValue();
								
								else if(childrenOfNode.item(j).getAttributes().getNamedItem("rdf:resource").getNodeValue() != null) {
									OWLRestrictions[t].onProperty = childrenOfNode.item(j).getAttributes().getNamedItem("rdf:resource").getNodeValue();
									OWLRestrictions[t].onProperty = OWLRestrictions[t].onProperty.substring(OWLRestrictions[t].onProperty.indexOf("#") + 1, OWLRestrictions[t].onProperty.length());
								}
							
								OWLRestrictions[t].onPropertyType = "owl:ObjectProperty";
							}	
						}
					}
					
					else {
						OWLRestrictions[t].onProperty = onPropertyNode.getAttributes().getNamedItem("rdf:resource").getNodeValue();
						OWLRestrictions[t].onProperty = OWLRestrictions[t].onProperty.substring(OWLRestrictions[t].onProperty.indexOf("#") + 1, OWLRestrictions[t].onProperty.length());
					}
				}
				
				childrenOfNode = listOfNodes.item(k).getChildNodes();
				
				if(childrenOfNode.item(i).getNodeName().compareTo("owl:maxCardinality") == 0 || childrenOfNode.item(i).getNodeName().compareTo("owl:maxQualifiedCardinality") == 0) {
					
					OWLRestrictions[t].cardinalityType = "owl:maxCardinality";
					OWLRestrictions[t].EcorelowerBound = 1;
					OWLRestrictions[t].EcoreupperBound = 1;					
				}
				
				childrenOfNode = listOfNodes.item(k).getChildNodes();
				
				if(childrenOfNode.item(i).getNodeName().compareTo("owl:minCardinality") == 0 || childrenOfNode.item(i).getNodeName().compareTo("owl:minQualifiedCardinality") == 0) {
					
					OWLRestrictions[t].cardinalityType = "owl:minCardinality";
					OWLRestrictions[t].EcorelowerBound = 1;
					OWLRestrictions[t].EcoreupperBound = -1;
				}
				
				childrenOfNode = listOfNodes.item(k).getChildNodes();
				
				if(childrenOfNode.item(i).getNodeName().compareTo("owl:cardinality") == 0) {
					
					OWLRestrictions[t].cardinalityType = "owl:cardinality";
					OWLRestrictions[t].EcorelowerBound = 0;
					OWLRestrictions[t].EcoreupperBound = 1;
				}
			
			}
			
			t++;
			
			childrenOfNode = listOfNodes;
			
		}
		
	//	for(int i = 0; i < countOfOWLRestrictions; i++) {
			
	//		System.out.println(OWLrestrictions[i].onProperty );
	//		System.out.println(OWLrestrictions[i].onPropertyType);
	//		System.out.println(OWLrestrictions[i].cardinalityType);
	//		System.out.println(OWLrestrictions[i].EcoreupperBound);

	//		System.out.println(" ");
	//	}
		
		mapCardinalitiesToEcore();
	}

	public void mapCardinalitiesToEcore() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	// Create instance of document that will store .xmi model file 
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document ecoreModel = docBuilder.parse(pathEcoreModel);
		
		NodeList listOfNodes = ecoreModel.getElementsByTagName("eStructuralFeatures"); // Store list of all nodes in that exist in model file. 
		NodeList childrenOfNode = listOfNodes;
		NamedNodeMap attrOfNode = null;
	
		String memberTypeVal = "null";
		
		Map<String, String> datatypeMappingOWL2Ecore = createDatatypeMappingTableOWL2Ecore();
		
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
		EReferences = new EReferenceProperties[countOfEReferences];
		
		int c = 0;
		int d = 0;
		
		childrenOfNode = listOfNodes;
		attrOfNode = null;
				
		for(int i = 0; i < listOfNodes.getLength(); i++) {
			
			attrOfNode = listOfNodes.item(i).getAttributes();
			
			if(attrOfNode.getNamedItem("xsi:type") != null) 
				memberTypeVal = attrOfNode.getNamedItem("xsi:type").getNodeValue().substring(6, attrOfNode.getNamedItem("xsi:type").getNodeValue().length());
			
			if(memberTypeVal.compareTo("EAttribute") == 0) 
			{
				EAttributes[c] = new EAttributeProperties();
				
				EAttributes[c].index = i;
				
				if(attrOfNode.getNamedItem("name") != null) {
					
					EAttributes[c].Name = attrOfNode.getNamedItem("name").getNodeValue();
						
					for(int r = 0; r < countOfOWLDatatypeProperties; r++) {
						
						if(EAttributes[c].Name.compareTo(OWLDatatypeproperties[r].rdfAbout) == 0 ) 						
							EAttributes[c].EType = datatypeMappingOWL2Ecore.get(OWLDatatypeproperties[r].Range);
						
					} 
					
					for(int w = 0; w < countOfOWLRestrictions; w++) {
						
						EAttributes[c].Lower_Bound = String.valueOf(0);
						EAttributes[c].Upper_Bound = String.valueOf(1);
						
						if(EAttributes[c].Name.compareTo(OWLRestrictions[w].onProperty) == 0 ) {
						
								EAttributes[c].Lower_Bound = String.valueOf(OWLRestrictions[w].EcorelowerBound);
								EAttributes[c].Upper_Bound = String.valueOf(OWLRestrictions[w].EcoreupperBound);
						}
					} 
					 
				}
				
				if(attrOfNode.getNamedItem("lowerBound") != null) {
					attrOfNode.getNamedItem("lowerBound").setNodeValue(EAttributes[c].Lower_Bound);
				}
				
				else {
					
					Attr lowerBoundAttr = ecoreModel.createAttribute("lowerBound");
					lowerBoundAttr.setNodeValue(EAttributes[c].Lower_Bound);
					
					Node parentNode = listOfNodes.item(i);
								
					Element parentNod = (Element)parentNode;
					parentNod.setAttributeNode(lowerBoundAttr);
					
				}
				
				if(attrOfNode.getNamedItem("upperBound") != null) {
					attrOfNode.getNamedItem("upperBound").setNodeValue(EAttributes[c].Upper_Bound);
				}
				
				else {
					
					Attr upperBoundAttr = ecoreModel.createAttribute("upperBound");
					upperBoundAttr.setNodeValue(EAttributes[c].Upper_Bound);
					
					Node parentNode = listOfNodes.item(i);
								
					Element parentNod = (Element)parentNode;
					parentNod.setAttributeNode(upperBoundAttr);
					
				}
				
				if(attrOfNode.getNamedItem("changeable") != null) 
					 EAttributes[c].Changeable = attrOfNode.getNamedItem("changeable").getNodeValue();
				
				if(attrOfNode.getNamedItem("defaultValueLiteral") != null) 
					 EAttributes[c].Default_Value_Literal = attrOfNode.getNamedItem("defaultValueLiteral").getNodeValue();
				 
				if(attrOfNode.getNamedItem("iD") != null) 
					 EAttributes[c].ID = attrOfNode.getNamedItem("iD").getNodeValue();
													
				if(attrOfNode.getNamedItem("ordered") != null) 
					 EAttributes[c].Ordered = attrOfNode.getNamedItem("ordered").getNodeValue();
			
				if(attrOfNode.getNamedItem("transient") != null) 
					 EAttributes[c].Transient = attrOfNode.getNamedItem("transient").getNodeValue();
				
				if(attrOfNode.getNamedItem("unique") != null) 
					 EAttributes[c].Unique = attrOfNode.getNamedItem("unique").getNodeValue();
				
				if(attrOfNode.getNamedItem("unsettable") != null) 
					 EAttributes[c].Unsettable = attrOfNode.getNamedItem("unsettable").getNodeValue();
				
				if(attrOfNode.getNamedItem("volatile") != null) 
					 EAttributes[c].Volatile = attrOfNode.getNamedItem("volatile").getNodeValue();
						
				if(attrOfNode.getNamedItem("eType") != null) {
						Node childMemberAttri = attrOfNode.getNamedItem("eType");
						String EType = "ecore:EDataType http://www.eclipse.org/emf/2002/Ecore#//" + EAttributes[c].EType;
						
						childMemberAttri.setNodeValue(EType);
				}
					
				c++;
				
				
				
			}
			
			
			if(memberTypeVal.compareTo("EReference") == 0) 
			{
				EReferences[d] = new EReferenceProperties();
				
				EReferences[d].Lower_Bound = String.valueOf(0);
				EReferences[d].Upper_Bound = String.valueOf(-1);
							
				if(attrOfNode.getNamedItem("name") != null) {
					
					EReferences[d].Name = attrOfNode.getNamedItem("name").getNodeValue();
						
					for(int r = 0; r < countOfOWLRestrictions; r++) {
						
						if(EReferences[d].Name.compareTo(OWLRestrictions[r].onProperty) == 0 ) {
						
							EReferences[d].Lower_Bound = String.valueOf(OWLRestrictions[r].EcorelowerBound);
							EReferences[d].Upper_Bound = String.valueOf(OWLRestrictions[r].EcoreupperBound);
														
						}
						
					}
					

					for(int v = 0; v < countOfOWLObjectProperties; v++) {
						
						if(EReferences[d].Name.compareTo(OWLObjectproperties[v].name) == 0 ) {
						
							if(OWLObjectproperties[v].owlComment != null) {
								EReferences[d].Containment = OWLObjectproperties[v].owlComment;
								EReferences[d].Containment = EReferences[d].Containment.substring(14, EReferences[d].Containment.length());
							}
						}
					}
	
				}

				if(attrOfNode.getNamedItem("eType") != null) {
					EReferences[d].EType = attrOfNode.getNamedItem("eType").getNodeValue();
					EReferences[d].EType = EReferences[d].EType.substring(3, EReferences[d].EType.length());
				}
				
				if(attrOfNode.getNamedItem("lowerBound") != null) {
					attrOfNode.getNamedItem("lowerBound").setNodeValue(EReferences[d].Lower_Bound);
				}
				
				else {
					
					Attr lowerBoundAttr = ecoreModel.createAttribute("lowerBound");
					lowerBoundAttr.setNodeValue(EReferences[d].Lower_Bound);
					
					Node parentNode = listOfNodes.item(i);
								
					Element parentNod = (Element)parentNode;
					parentNod.setAttributeNode(lowerBoundAttr);
					
				}
				
				
				if(attrOfNode.getNamedItem("upperBound") != null) {
					attrOfNode.getNamedItem("upperBound").setNodeValue(EReferences[d].Upper_Bound);
				}
				
				else {
					
					Attr upperBoundAttr = ecoreModel.createAttribute("upperBound");
					upperBoundAttr.setNodeValue(EReferences[d].Upper_Bound);
					
					Node parentNode = listOfNodes.item(i);
								
					Element parentNod = (Element)parentNode;
					parentNod.setAttributeNode(upperBoundAttr);
					
				}
							
				if(attrOfNode.getNamedItem("containment") != null && EReferences[d].Containment != null) {
					attrOfNode.getNamedItem("containment").setNodeValue(EReferences[d].Containment);
				}
				
				else if(EReferences[d].Containment != null) {
					
					Attr containment = ecoreModel.createAttribute("containment");
					containment.setNodeValue(EReferences[d].Containment);
					
					Node parentNode = listOfNodes.item(i);
								
					Element parentNod = (Element)parentNode;
					parentNod.setAttributeNode(containment);
					
				}
							
				d++;
				
			}
			
			
			
		}	
		
		for(int i = 0; i < countOfEAttributes; i++) {
			
		//	System.out.println(EAttributes[i].index+1 + "   " + EAttributes[i].Name);
		//	System.out.println(i+1 + "   " + EAttributes[i].Default_Value_Literal);
		//	System.out.println(i+1 + "   " + EAttributes[i].EType);
		//	System.out.println(i+1 + "   " + EAttributes[i].ID);
		//	System.out.println(" ");
		} 
		
	    listOfNodes = ecoreModel.getElementsByTagName("eClassifiers");

		childrenOfNode = listOfNodes;
		attrOfNode = null;
		
	    for(int p = 0; p < countOfOWLDatatypeProperties; p++) {
			
		    for(int i = 0; i < listOfNodes.getLength(); i++) {
				
				attrOfNode = listOfNodes.item(i).getAttributes();
				
				if(attrOfNode.getNamedItem("name") != null) 
				{				
					if( OWLDatatypeproperties[p].Range != null) {
						String OWLdataType = OWLDatatypeproperties[p].Range;
						OWLdataType = OWLdataType.substring(OWLdataType.lastIndexOf("#") + 1, OWLdataType.length());				
				
						if(attrOfNode.getNamedItem("name").getNodeValue().compareTo(OWLdataType) == 0) {
							childrenOfNode.item(i).getParentNode().removeChild(childrenOfNode.item(i));
						}	
				
					}				
				}				
			}
	    }
		
		ecoreModel.getDocumentElement().normalize();
	    
		TransformerFactory transformerFactory = TransformerFactory.newInstance();	
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(ecoreModel);			// Create a copy of the .xmi model file
        StreamResult result = new StreamResult(new File(pathEcoreModel));	// Set file path for the updated .xmi model file in result
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);			// Save the updated .xmi model file in result.
        System.out.println("XML file updated successfully");

		
	}
	
	public static Map<String, String> createDatatypeMappingTableOWL2Ecore() {
		
		Map<String, String> datatypeMappingOWL2Ecore = new HashMap<>();
		
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#string", "EString");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#int", "EInt");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#boolean", "EBoolean");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#byte", "EByte");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#hexBinary", "EByteArray");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#byte", "EByteObject");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#date", "EDate");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#dateTime", "EDate");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#double", "EDouble");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#float", "EFloat");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#long", "ELong");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#short", "EShort");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#decimal", "EBigDecimal");
		datatypeMappingOWL2Ecore.put("http://www.w3.org/2001/XMLSchema#integer", "EBigInteger");
		
		return datatypeMappingOWL2Ecore;
	}
	
}

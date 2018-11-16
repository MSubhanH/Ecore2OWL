package CardinalityMapper;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class mapCardinality{
		
	public mapCardinality(String transformDirection, String path1, String path2) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		if(transformDirection.compareTo("ecore2owl") == 0) {			
			MapEcore2OWL mapEcore2OWL = new MapEcore2OWL(path1, path2);	
		}			
		
		if(transformDirection.compareTo("owl2ecore") == 0) {
			MapOWL2Ecore mapOWL2Ecore = new MapOWL2Ecore(path1, path2);
		}
		
		if(transformDirection.compareTo("updateOWLInstance") == 0) {
			UpdateOWLInstance updateOWLInstance = new UpdateOWLInstance(path1, path2);
		}
	}
}

package CardinalityMapper;

public class CardinalityMapper_main {
	
	public static final String pathEcoreModel = "E:\\Git Projects\\emf4sw\\examples\\com.emf4sw.owl.examples\\src\\Family.ecore";		
	public static final String pathOWLOntology = "E:\\Git Projects\\emf4sw\\examples\\com.emf4sw.owl.examples\\src\\family.owl";	
		
	public static void main(String args[]) {
		
		try {
					mapCardinality newEcoreToOWLMapper = new mapCardinality("ecore2owl", pathEcoreModel, pathOWLOntology);
					mapCardinality newOWLToEcoreMapper = new mapCardinality("owl2ecore", pathEcoreModel, pathOWLOntology);  		
		}
		
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
	}
	

}

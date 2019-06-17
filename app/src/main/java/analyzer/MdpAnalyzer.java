package analyzer;

import metrohm.omnis.IOmnisNode;

/**
 * Verifiziert ob alle ben�tigten Resourcen vorhanden und mit der Versionsnummer des Root Node �bereinstimmen.
 * 
 * @author due/sjam
 */
public class MdpAnalyzer implements IOmnisNodeAnalyzer {

	@Override
	public String name() {
		return "Mdp";
	}

	@Override
	public boolean analyze(IOmnisNode node) {
		return true;
	}	
	
	@Override
	public void display(IOmnisNode node, IResultProcessor resultProcessor) {
		resultProcessor.title("Not Yet Implemented");
	}
}

package analyzer;

import metrohm.omnis.IOmnisNode;

/**
 * Verifiziert ob alle Mdf Files korrekt sind.
 * 
 * @author due/sjam
 */
public class MdfAnalyzer implements IOmnisNodeAnalyzer {
	
	@Override
	public String name() {
		return "Mdf";
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
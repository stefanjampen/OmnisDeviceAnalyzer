package analyzer;

import java.util.ArrayList;
import java.util.List;

import metrohm.omnis.IOmnisNode;

/**
 * Beinhaltet die verf�gbaren NodeAnalyzer. Ist als Singleton implementiert.
 *  
 * @author due
 */
public class NodeAnalyzers {
	private static NodeAnalyzers instance;
	private List<IOmnisNodeAnalyzer> analyzers;
	
	private NodeAnalyzers() {		
		analyzers = new ArrayList<IOmnisNodeAnalyzer>();
	}
	
	public static NodeAnalyzers instance() {
		if (instance == null) {
			instance = new NodeAnalyzers();
		}
		return instance;		
	}	
	
	/**
	 * Fuegt einen Analyzer hinzu.
	 * 
	 * @param analyzer
	 */
	public void addAnalyzer(IOmnisNodeAnalyzer analyzer) {		
		analyzers.add(analyzer);
	}
		
	/**
	 * Zugriff auf die verschiedenen Analyzer
	 *
	 * @return analyzer
	 */
	public List<IOmnisNodeAnalyzer> getAnalyzers() {
		return analyzers;
	}
	
	/**
	 * Analysiert einen Node mit all seinen Kindern und allen verfügbaren Analysatoren.
	 * Setzt dabei die Gültigkeit der OmnisNodes. Wird typischerweise im Connect Fall gemacht.
	 * 
	 * @param root
	 */
	public void analyze(IOmnisNode root, IAnalyzerProgress progress) {
		root.setValid(doAnalyze(root, progress));
	}	
	
	private boolean doAnalyze(IOmnisNode node, IAnalyzerProgress progress) {
		boolean ok = true;
		for (IOmnisNodeAnalyzer na : analyzers) {
			progress.analyze(node, na.name());
			ok = na.analyze(node);
			for (IOmnisNode on: node.children()) {
				ok = ok && doAnalyze(on, progress);
			}
			node.setValid(ok);
		}
		return ok;
	}
}

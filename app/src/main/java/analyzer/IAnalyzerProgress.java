package analyzer;

import metrohm.omnis.IOmnisNode;

/**
 * Möglichkeit bei einer langandauernde Analyse Progress Information auszugeben.
 *  
 * @author due
 */
public interface IAnalyzerProgress {

	/**
	 * Wird für jeden Knoten und Analyzer aufgerufen
	 * 
	 * @param node Node zum analysieren
	 * @param analyzer Name des Analyzers
	 */
    void analyze(IOmnisNode node, String analyzer);
}

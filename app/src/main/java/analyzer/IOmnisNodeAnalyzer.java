package analyzer;

import metrohm.omnis.IOmnisNode;

/**
 * Schnittstelle für eine Analyse. Eine Ananlyse besteht aus dem Durchführen irgendwelcher Tests auf einem Node und seinen Kindern.
 * Typischerweise wird die Ananlyse jeweils auf dem Top Node durchgeführt.
 * 
 * Die Resultate, werden dann Node spezifisch dargestellt.
 * 
 * @author due
 */
public interface IOmnisNodeAnalyzer {
	
	/**
	 * Bezeichnet die Analyse. Wird primär für die Beschriftung des Tabs verwendet.
	 * 
	 * @return Name des Analyzers
	 */
    String name();
			
	/**
	 * F�hrt die Analyse auf dem Node und seinen Kindern aus.
	 * 
	 * @param node der zu analysierende Node
	 * @return true, wenn alles paletti ist. 
	 */
	boolean analyze(IOmnisNode node);
	
	/**
	 * Zeigt die Resultate des entsprechenden Node an.
	 * 
	 * @param node entsprechender Node
	 * @param resultProcessor wird benötigt für die Ausgabe der Daten
	 */
	void display(IOmnisNode node, IResultProcessor resultProcessor);
}

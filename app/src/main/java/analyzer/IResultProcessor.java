package analyzer;


/**
 * Interface für die Verarbeitung der Daten eines Analyzers.
 * Es hat im Moment nur beschränkte Möglichkeiten.
 * 
 * @author due
 */
public interface IResultProcessor {
	
	/**
	 * Gibt einen Titel aus.
	 * 
	 * @param title Text des Titels
	 */
	void title(String title /*Color color */);
	
	/**
	 * Gibt eine Kopfzeile aus
	 * 
	 * @param headerRow Array von Kopfzeile
	 */
	void headRow(String[] headerRow);
		
	/**
	 * Gibt eine Kopfzeile aus
	 * 
	 * @param dataRow Array von Datenreihen
	 */
	void dataRow(String[] dataRow);
	
	/**
	 * Trennung
	 */
	void seperator();
}

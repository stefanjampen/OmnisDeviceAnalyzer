package metrohm.omnis;

import java.util.List;

/**
 * Repraesentiert ein Omnis Device. Dazu gehoeren MN & CN Knoten.
 * 
 * @author due
 */
public interface IOmnisNode {	
	int getType();
	String getName();
	String getMacAddress();
	String getIpAddress();
	int getPort();
	boolean isInternal();
	boolean isUnbound();
	boolean isValid();
	void setValid(boolean b);
	List<IOmnisNode> children();
}

package metrohm.omnis;

import java.util.List;

/**
 * Repraesentiert den Zugriff auf ein Omnis System.
 * 
 * @author due
 */
public interface IOmnisSystem {
	List<IOmnisNode> scanNodes();
	void identify(IOmnisNode node);
	void connectNode(IOmnisNode node);
}

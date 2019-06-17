package metrohm.omnis;

import java.util.List;

public interface IOmnisAdapterReply {
    void nodeScanFinished (List<IOmnisNode> nodes);
    void setTreeFragment(IOmnisNode mNode);
}

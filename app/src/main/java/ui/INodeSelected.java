package ui;

import metrohm.omnis.IOmnisNode;

/**
 * An interface which defines the callback method used for the communication between the DeviceStructureFragment and the MainActivity.
 */
public interface INodeSelected {
    void onNodeSelected(IOmnisNode omnisNode);
}

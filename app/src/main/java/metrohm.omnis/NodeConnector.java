package metrohm.omnis;

import android.os.AsyncTask;
import com.metrohm.mdp.client.Device;
import com.metrohm.mdp.client.DeviceScan;

/**
 * Connects the selected node from the scanned nodes. The connection is done in an AsyncTask.
 */
public class NodeConnector extends AsyncTask<Void, Void, Void> {

    private IOmnisNode node;
    private DeviceScan deviceScan;
    private IConnectionFinished os;

    NodeConnector(IOmnisNode node, IOmnisSystem os, DeviceScan ds) {
        this.node = node;
        deviceScan = ds;
        this.os = (IConnectionFinished) os;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        os.connectionFinished();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        deviceScan.scanDevices(new DeviceScan.IDeviceFilter() {
            @Override
            public boolean isValidDevice(Device dev) {
                return dev.getMac().toString().equals(node.getMacAddress());
            }
        });
        return null;
    }
}

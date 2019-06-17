package metrohm.omnis;

import android.os.AsyncTask;

import com.metrohm.mdp.client.Device;
import com.metrohm.mdp.client.DeviceScan;

import java.util.ArrayList;


/**
 * Starts an identify procedure with the selected node. Is done in an AsyncTask.
 */
public class NodeIdentifier extends AsyncTask<Void, Void, Void> {
    private DeviceScan deviceScan;
    private ArrayList<Device> mdpDevices;

    NodeIdentifier (DeviceScan ds, ArrayList<Device> mdpDevices){
        this.deviceScan = ds;
        this.mdpDevices = mdpDevices;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        deviceScan.identify(mdpDevices.get(0));
        return null;
    }
}

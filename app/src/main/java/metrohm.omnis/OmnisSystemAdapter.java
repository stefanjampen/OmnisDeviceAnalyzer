package metrohm.omnis;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.metrohm.mdp.client.Device;
import com.metrohm.mdp.client.DevicePool;
import com.metrohm.mdp.client.DeviceScan;
import com.metrohm.mdp.client.SubDevice;
import com.metrohm.mdp.generated.ISystem;
import com.metrohm.mdp.generated.SystemProxy;
import com.metrohm.mdp.resources.ListField;
import com.metrohm.utils.MacAddress;

/**
 * An adapter class, which provides all information from the MDP library which is used to display in the app.
 */
public class OmnisSystemAdapter extends AsyncTask<Void, Void, Object> implements IOmnisSystem, IConnectionFinished{

	private DeviceScan deviceScan;
	private List<IOmnisNode> omnisNodeList;
	private ArrayList<Device> mdpDeviceList;
	private HashMap<String, IOmnisNode> omnisNodeMap;
	private IOmnisAdapterReply adapterReply;

	public OmnisSystemAdapter(int port, String ip, IOmnisAdapterReply oar) {
		deviceScan = new DeviceScan(port, ip);
		this.adapterReply = oar;
	}

	@Override
	public List<IOmnisNode> scanNodes() {

		mdpDeviceList = deviceScan.scanDevices();
		omnisNodeList = new ArrayList<>();

		int port = 1111;
		for (Device d: mdpDeviceList) {
			omnisNodeList.add(new OmnisNode(d.getType(), d.getName(), d.getMac().toString(), d.getIpAddress(), port, true));
		}

		return omnisNodeList;
	}

    /**
     * Scans the nodes in the background.
     * @return scans the network an returns a list of nodes
     */
	@Override
	protected List doInBackground(Void... voids) {
		 return scanNodes();
	}

    /**
     * Is being executed after the searching for nodes in the network (AsynkTask) is finished.
     * @param o list of available nodes
     */
	@Override
	protected void onPostExecute(Object o) {
		super.onPostExecute(o);
		adapterReply.nodeScanFinished((List<IOmnisNode>) o);
	}

    /**
     * Starts an identify procedure over the network. Is done in an AsynkTask.
     * @param node node to identify
     */
	@Override
	public void identify(IOmnisNode node) {
		NodeIdentifier nodeIdentifier = new NodeIdentifier(deviceScan, mdpDeviceList);
		nodeIdentifier.execute();
	}

    /**
     * Starts a Connection with a node. Is done in an AsyncTask.
     * @param node node which gets connected
     */
	@Override
	public void connectNode(final IOmnisNode node) {
		NodeConnector nodeConnector = new NodeConnector(node, this, deviceScan);
		nodeConnector.execute();
	}

    /**
     * Gets called when the connection is finished. Sets the Managing Node/ root Node. Communicates with the MainActivty.
     */
	@Override
	public void connectionFinished() {
		// Gibt für alle gekrallten Geräte die Topologie aus --> DeviceFilter lässt immer nur ein Device in den DevicePool aufnehmen
		Device titrator = DevicePool.getInstance().getDevices().get(0);

		IOmnisNode managingNode;
		omnisNodeMap = new HashMap<>();
		managingNode = makeOmnisNode(titrator);
		omnisNodeMap.put(managingNode.getMacAddress(), managingNode);

		if (omnisNodeList.isEmpty()) {
			error();
		}

		adapterReply.setTreeFragment(generateOmnisTree(managingNode, titrator));
	}

	private IOmnisNode generateOmnisTree (IOmnisNode topNode, SubDevice device) {
		ListField subElements = (ListField) device.getSystemProxy().getField(ISystem.SUBELEMENTS);
		for (int i = 0; i < subElements.getMaxIndex(); i++) {
			subElements.setIndex(i);
			String macAdress = subElements.getField(ISystem.MACADDRESS).getValue();
			SubDevice subDevice = ((Device) device).getSubDeviceByMac(new MacAddress(macAdress));

			OmnisNode childNode = makeOmnisNode(subDevice);
			omnisNodeMap.put(childNode.getMacAddress(), childNode);

			String parentMac = subElements.getField(ISystem.PARENTMACADDRESS).getValue();

			omnisNodeMap.get(parentMac).children().add(childNode);
        }

		return topNode;
	}

	private OmnisNode makeOmnisNode (SubDevice subDevice) {
		SystemProxy sp = subDevice.getSystemProxy();

		return new OmnisNode(Integer.parseInt(sp.getField(ISystem.TYPE).getValue()),
				sp.getField(ISystem.NAME).getValue(),
				sp.getField(ISystem.MACADDRESS).getValue(),
				sp.getField(ISystem.CLIENTIP).getValue(), //anpassen
				Integer.parseInt(sp.getField(ISystem.PARENTPORT).getValue()),
				true,
				subDevice
		);
	}

	private static void error () {
		System.out.println("error: no titrator found" );
		System.exit(-1);
	}

	public class OmnisNode implements IOmnisNode {

		private int type;
		private String name;
		private String mac;
		private String ip;
		private int port;
		private boolean internal;
		private List<IOmnisNode> children;

		private SubDevice subDevice;

		OmnisNode(int type, String name, String mac, String ip, int port, boolean internal, SubDevice subDevice) {
			this(type, name, mac, ip, port, internal);
			this.subDevice = subDevice;
		}

		OmnisNode(int type, String name, String mac, String ip, int port, boolean internal) {
			this.type = type;
			this.mac = mac;
			this.name = name.replaceAll("&apos;", "'");
			this.ip = ip;
			this.port = port;
			this.internal = internal;

			children = new ArrayList<>();
		}

		@Override
		public int getType() {

			return type;
		}

		@Override
		public String getName() {

			return name;
		}

		@Override
		public String getMacAddress() {
			return mac;
		}

		@Override
		public String getIpAddress() {

			return ip;
		}

		@Override
		public int getPort() {
			return port;
		}

		@Override
		public boolean isInternal() {
			return internal;
		}

		@Override
		public boolean isUnbound() {
			return false;
		}

		@Override
		public boolean isValid() {
			return false;
		}

		@Override
		public void setValid(boolean b) {

		}

		@Override
		public List<IOmnisNode> children() {
			return children;
		}

		public SubDevice getSubDevice() {
			return subDevice;
		}


		public String getFirmWareVersion() {
			return subDevice.getSystemProxy().getValue(ISystem.FIRMWARE);
		}

		public String getNodeState() {
			return subDevice.getSystemProxy().getValue(ISystem.STATEINDICATION);
		}


		public String getSerialNumber() {
			return subDevice.getSystemProxy().getValue(ISystem.BARCODE);
		}
	}
}
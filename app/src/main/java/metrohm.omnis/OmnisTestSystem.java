package metrohm.omnis;

import java.util.ArrayList;
import java.util.List;

import metrohm.omnis.IOmnisNode;
import metrohm.omnis.IOmnisSystem;

/**
 * Test Klasse zum Erarbeiten der Grundlagen des UI's.
 * 
 * @author due
 */
public class OmnisTestSystem implements IOmnisSystem {

	@Override
	public List<IOmnisNode> scanNodes() {
		ArrayList<IOmnisNode> al = new ArrayList<>();
		
		al.add(new OmnisNode(1, "MN IP", "00:12:13:00:00:2D", "10.156.35.208", 2, true));
		al.add(new OmnisNode(1, "MN IP", "00:12:13:00:01:00", "192.168.1.120", 4, true));
		
		return al;
	}

	@Override
	public void identify(IOmnisNode node) {
		System.out.println("identify: " + node.toString());
	}

	@Override
	public void connectNode(IOmnisNode node) {
		
		OmnisNode root = (OmnisNode) node;
		
		if (node.children() == null){
			
			root.add(new OmnisNode(2, "Stirrer IP", "00:12:13:00:01:04", "192.168.1.125", 2, false));

            OmnisNode abcModule = new OmnisNode(4, "Dosing Module", "00:12:13:00:01:03", "192.168.1.123", 1, false);
			OmnisNode dosingModule = new OmnisNode(4, "Dosing Module", "00:12:13:00:01:03", "192.168.1.123", 1, false);
			dosingModule.add(new OmnisNode(2, "Dos IP", "00:12:13:00:01:09", "192.168.1.129", 6, true));
			dosingModule.add(new OmnisNode(2, "Stirrer IP", "00:12:13:00:01:08", "192.168.1.128", 6, true));
            abcModule.add(new OmnisNode(2, "Stirrer IP", "00:12:13:00:01:08", "192.168.1.128", 6, true));
            abcModule.add(new OmnisNode(2, "Stirrer IP", "00:12:13:00:01:08", "192.168.1.128", 6, true));
            abcModule.add(new OmnisNode(2, "Stirrer IP", "00:12:13:00:01:08", "192.168.1.128", 6, true));
			root.add(dosingModule);
			root.add(abcModule);
		}
	}
	
	public class OmnisNode implements IOmnisNode {
		
		private int type;
		private String name;
		private String mac;
		private String ip;
		private int port;
		private boolean internal;
		private List<IOmnisNode> children;
		
		public OmnisNode(int type, String name, String mac, String ip, int port, boolean internal) {
			this.type = type;
			this.mac = mac;
			this.name = name;
			this.ip = ip;
			this.port = port;
			this.internal = internal;			
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
			return (port & 1) != 0;
		}

		@Override
		public boolean isValid() {
			return port < 3;
		}

		@Override
		public void setValid(boolean b) {

		}

		@Override
		public List<IOmnisNode> children() {
			return children;
		}

		public void add(IOmnisNode node) {
			if (children == null) {
				children = new ArrayList<IOmnisNode>();
			}
			children.add(node);
		}
		
		@Override
		public String toString() {
			return name + " : " + ip;
		}
	}
}

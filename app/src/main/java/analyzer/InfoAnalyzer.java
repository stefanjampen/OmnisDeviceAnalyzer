package analyzer;

import com.metrohm.mdp.client.SubDevice;
import com.metrohm.mdp.generated.ISystem;
import com.metrohm.mdp.generated.SystemProxy;
import com.metrohm.mdp.resources.ListField;

import metrohm.omnis.IOmnisNode;
import metrohm.omnis.OmnisSystemAdapter;

/**
 * Standard NodeAnanlyzer, der keine echte Ananlyse macht, sondern nur die in der Aufgabenstellung 
 * geforderte Information pro Node ausgibt.
 * 
 * @author due/sjam
 */
public class InfoAnalyzer implements IOmnisNodeAnalyzer {

	@Override
	public String name() {
		return "Info";
	}

	@Override
	public boolean analyze(IOmnisNode node) {
		return true;
	}	
	
	@Override
	public void display(IOmnisNode node, IResultProcessor resultProcessor) {
		OmnisSystemAdapter.OmnisNode omnisNode = (OmnisSystemAdapter.OmnisNode) node;
		
		resultProcessor.title("Properties");
		resultProcessor.dataRow(new String[] { "Name", node.getName() });
		resultProcessor.dataRow(new String[] { "MAC-Adress", node.getMacAddress() });
		resultProcessor.dataRow(new String[] { "IP-Adress", node.getIpAddress() });
		resultProcessor.dataRow(new String[] { "FW-Version", omnisNode.getFirmWareVersion() });
		
		resultProcessor.title("Crash-Dump");
		
		SubDevice subDevice = omnisNode.getSubDevice();
		resultProcessor.headRow(new String[] { "Index", "BootCount", "Zeile" });
		
		SystemProxy sp = subDevice.getSystemProxy();
		sp.execute(ISystem.GETCRASHDUMP_ACTION);
		ListField crashDump = (ListField) sp.getField(ISystem.CRASHLOG);
		for (int i = 0; i < crashDump.getMaxIndex(); i++) {
			crashDump.setIndex(i);
			resultProcessor.dataRow(new String[] { Integer.toString(i), crashDump.getField(ISystem.CRASHBOOTCOUNT).getValue(), crashDump.getField(ISystem.CRASHINFO).getValue()});	
		}
		
		resultProcessor.dataRow(new String[] { "Node-State", omnisNode.getNodeState() });
		resultProcessor.dataRow(new String[] { "Serial-Number", omnisNode.getSerialNumber() });
	}
}

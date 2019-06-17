package ui;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a0010211.omnisdeviceanalyzer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import analyzer.IOmnisNodeAnalyzer;
import analyzer.IResultProcessor;
import analyzer.InfoAnalyzer;
import analyzer.MdfAnalyzer;
import analyzer.MdpAnalyzer;
import analyzer.NodeAnalyzers;
import metrohm.omnis.IOmnisAdapterReply;
import metrohm.omnis.IOmnisNode;
import metrohm.omnis.IOmnisSystem;
import metrohm.omnis.OmnisSystemAdapter;

/**
 * The only existing activity which is responsible for the UI.
 */
public class MainActivity extends AppCompatActivity implements IOmnisAdapterReply, INodeSelected {

    //UI components
    private Button scanButton;
    private ProgressBar scanProgress , progressConnect;
    private TextView info;
    private Spinner dropDown;
    private String connectedDevice;
    private Fragment treeFragment;

    private IOmnisSystem omnisSystem;
    public static IOmnisNode managingNode;
    private HashMap<String, IOmnisNode> nodeHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //buttons
        scanButton = findViewById(R.id.scan_button);
        Button identifyButton = findViewById(R.id.identify_button);
        Button connectButton = findViewById(R.id.connect_button);

        scanButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                scanNodes();
            }

        });

        scanProgress = findViewById(R.id.progress_scan);
        scanProgress.setVisibility(View.GONE);
        identifyButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                identify();
            }
        });
        connectButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                connect();
            }
        });
        progressConnect = findViewById(R.id.progress_connect);
        progressConnect.setVisibility(View.GONE);

        //dropdown
        dropDown = findViewById(R.id.dropdown);
        connectedDevice = ""; // init

        info = findViewById(R.id.info);
        info.setVisibility(View.GONE);

        //setup of Analyzers
        NodeAnalyzers.instance().addAnalyzer(new InfoAnalyzer());
        NodeAnalyzers.instance().addAnalyzer(new MdpAnalyzer());
        NodeAnalyzers.instance().addAnalyzer(new MdfAnalyzer());
    }

    /**
     * Required to set up a callback from a fragment to the MainActivity. This method gets called from the OS.
     * @param fragment the fragment where the callback gets set
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof DeviceStructureFragment) {
            DeviceStructureFragment deviceStructureFragment = (DeviceStructureFragment) fragment;
            deviceStructureFragment.setIFragmentCallback(this);
        }
    }

    /**
     * Gets called when a node scan is finished. The node scan happens in an AsynkTask.
     * @param nodes a list of Omnis Nodes which are used to fill the Spinner
     */
    @Override
    public void nodeScanFinished( List<IOmnisNode> nodes) {
        scanButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                scanNodes();
            }

        });
        info.setVisibility(View.GONE);
        scanProgress.setVisibility(View.GONE);
        if (nodeHashMap == null){
            nodeHashMap = new HashMap<>();
            ArrayList<String> dropDownArray = new ArrayList<>();

            for(int i = 0; i<nodes.size(); i++){
               IOmnisNode omnisNode = nodes.get(i);
                String key = omnisNode.getName() + ": " + omnisNode.getIpAddress();

                nodeHashMap.put(key, omnisNode);
                dropDownArray.add(key);
            }

            ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, dropDownArray);
            dropdownAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            dropDown.setAdapter(dropdownAdapter);
        }
    }

    /**
     * Is a callback method and gets called when when the connection is finished. Creates the TreeFragment.
     * @param mNode the managing node/ root node which is used to create the tree fragment
     */
    @Override
    public void setTreeFragment(IOmnisNode mNode) {
        progressConnect.setVisibility(View.GONE);
        managingNode = mNode;
        Class fragmentClass = DeviceStructureFragment.class;
        treeFragment = Fragment.instantiate(this, fragmentClass.getName());
        Bundle args = new Bundle();
        treeFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, treeFragment, fragmentClass.getName()).commit();
    }

    /**
     * Is a callback function and gets called when a node in the TreeFragment was long clicked. Sets up the Analyzer Fragments with a TabLayout.
     * @param omnisNode the omnis node to analyze.
     */
    @Override
    public void onNodeSelected (IOmnisNode omnisNode) {
        ViewPager viewPager = findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4); //loads all fragments in the tabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        List<IOmnisNodeAnalyzer> nodeAnalyzers = NodeAnalyzers.instance().getAnalyzers();
        for (int i = 0; i < nodeAnalyzers.size(); i++) {
            IOmnisNodeAnalyzer nodeAnalyzer = nodeAnalyzers.get(i);

            IResultProcessor isr = adapter.getItem(i);

            nodeAnalyzer.display(omnisNode, isr);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK;
    }

    private void scanNodes(){
        //omnisSystem = new OmnisTestSystem();
        scanButton.setOnClickListener(null);
        String broadcast = "255.255.255.255";
        omnisSystem = new OmnisSystemAdapter(9114, broadcast, this);
        ((OmnisSystemAdapter)omnisSystem).execute();
        scanProgress = findViewById(R.id.progress_scan);
        scanProgress.setVisibility(View.VISIBLE);
    }

    private void identify(){
        if (dropDown.getSelectedItem() != null) {
            info.setVisibility(View.GONE);
            String key = dropDown.getSelectedItem().toString();
            omnisSystem.identify(nodeHashMap.get(key));
        }
        else {
            info.setVisibility(View.VISIBLE);
        }
    }

    private void connect() {
        if (dropDown.getSelectedItem() != null) {
            progressConnect.setVisibility(View.VISIBLE);
            String selectedDevice = dropDown.getSelectedItem().toString();
           if (!connectedDevice.equals(selectedDevice)) {
                connectedDevice = selectedDevice;
                managingNode = nodeHashMap.get(connectedDevice);
                omnisSystem.connectNode(managingNode);
            }
        }
        else {
            info.setVisibility(View.VISIBLE);
        }
        if(treeFragment != null){
            progressConnect.setVisibility(View.GONE);
        }
    }
}

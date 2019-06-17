package ui;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a0010211.omnisdeviceanalyzer.R;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.HashMap;

import metrohm.omnis.IOmnisNode;
/**
 * Created by Bogdan Melnychuk on 2/12/15.
 * The Fragment which is responsible for the "tree view".
 */
public class DeviceStructureFragment extends Fragment {
    private HashMap<String, IOmnisNode> nodeMap = new HashMap<>();

    private INodeSelected callback;

    public void setIFragmentCallback (INodeSelected callback){
       this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;
        rootView = View.inflate(getContext(), R.layout.fragment_default, null);
        ViewGroup containerView = rootView.findViewById(R.id.container);

        TreeNode root;
        root = TreeNode.root();
        createNodes(root , MainActivity.managingNode);

        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(DeviceTreeItemHolder.class);
        tView.setDefaultNodeLongClickListener(nodeLongClickListener);

        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    private void createNodes(TreeNode top , final IOmnisNode omnisNode){
        final TreeNode childNode;
        String nodeName = omnisNode.getName();
        nodeMap.put(nodeName, omnisNode);
        childNode = new TreeNode(new DeviceTreeItemHolder.IconTreeItem(R.string.ic_add_circle, nodeName, true));
        top.addChild(childNode);
        if(omnisNode.children() != null){
            for (int i = 0; i<omnisNode.children().size(); i++){
                createNodes(childNode, omnisNode.children().get(i));
            }
        }
    }

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            DeviceTreeItemHolder.IconTreeItem item = (DeviceTreeItemHolder.IconTreeItem) value;
            IOmnisNode oNode = nodeMap.get(item.text);

            callback.onNodeSelected(oNode);

            return true;
        }
    };
}

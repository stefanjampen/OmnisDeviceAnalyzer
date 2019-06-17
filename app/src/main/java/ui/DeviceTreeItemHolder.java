package ui;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.a0010211.omnisdeviceanalyzer.R;
import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 * Extended by SJAM. Describes the TreeItems which make up the 'tree view'.
 */
public class DeviceTreeItemHolder extends TreeNode.BaseNodeViewHolder<DeviceTreeItemHolder.IconTreeItem> {

    private PrintView arrowView;

    public DeviceTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        View view = View.inflate(context, R.layout.layout_device_node, null);
        TextView tvValue = view.findViewById(R.id.node_value);
        tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        if (! value.valid) {
            tvValue.setTextColor(Color.RED);
        }
        tvValue.setText(value.text);

        arrowView = view.findViewById(R.id.arrow_icon);
        arrowView.setIconColor(context.getResources().getColor(R.color.black));
        if (! value.valid) {
            arrowView.setVisibility(View.GONE);
        }

        if (node.isLeaf()){
            arrowView.setVisibility(View.GONE);
            value.icon = R.string.ic_remove_circle;
        }

        PrintView iconView = view.findViewById(R.id.icon);
        iconView.setIconText(context.getResources().getString(value.icon));
        iconView.setIconColor(context.getResources().getColor(R.color.colorPrimary));

        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public int icon;
        public String text;
        public boolean valid;

        IconTreeItem(int icon, String text, boolean valid) {
            this.icon = icon;
            this.text = text;
            this.valid = valid;
        }
    }
}

package ui;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.a0010211.omnisdeviceanalyzer.R;

import analyzer.IResultProcessor;


/**
 * A Fragment which is responsible for the analyzers.
 */
public class AnalyzerFragment extends android.support.v4.app.Fragment implements IResultProcessor {

    private TableLayout table;
    private Activity mainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_analyzer, container, false);

        table = view.findViewById(R.id.info_table);
        mainActivity = getActivity();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void title(String title) {

        TextView textView = new TextView(mainActivity);

        textView.setText(title);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
        textViewParams.weight = 1;
        textViewParams.topMargin = 3;
        textViewParams.bottomMargin = 3;

        textView.setLayoutParams(textViewParams);

        TableRow row = new TableRow(mainActivity);
        row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        row.addView(textView);
            table.addView(row);
    }

    @Override
    public void headRow(String[] headerRow) {
        TableRow row = new TableRow(mainActivity);

        for (String s: headerRow) {
            TextView textView = new TextView(mainActivity);
            textView.setText(s);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
            textViewParams.weight = 1;
            textViewParams.topMargin = 3;
            textViewParams.bottomMargin = 3;
            textView.setLayoutParams(textViewParams);

            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            row.addView(textView);
        }
        table.addView(row);
    }

    @Override
    public void dataRow(String[] dataRow) {
        TableRow row = new TableRow(mainActivity);

        for (String s: dataRow) {
            TextView textView = new TextView(mainActivity);
            textView.setText(s);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT);
            textViewParams.weight = 1;
            textViewParams.topMargin = 3;
            textViewParams.bottomMargin = 3;
            textView.setLayoutParams(textViewParams);

            row.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            row.addView(textView);
        }
        table.addView(row);
    }

    @Override
    public void seperator() {

    }
}

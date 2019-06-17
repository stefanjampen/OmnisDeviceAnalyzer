package ui;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import analyzer.IOmnisNodeAnalyzer;
import analyzer.NodeAnalyzers;

/**
 * An adapter class to set up the TabLayout with ViewPager.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private AnalyzerFragment infoFragment, mdpFragment, mdfFragment;

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public AnalyzerFragment getItem(int i) {
        switch (i) {
            case 0:     if (infoFragment == null) {infoFragment = new AnalyzerFragment();}
                return infoFragment;

            case 1:     if (mdfFragment == null) {mdfFragment = new AnalyzerFragment();}
                return mdfFragment;

            case 2:     if (mdpFragment == null) {mdpFragment = new AnalyzerFragment();}
                return mdpFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        List<IOmnisNodeAnalyzer> analyzers = NodeAnalyzers.instance().getAnalyzers();
        for (IOmnisNodeAnalyzer na : analyzers) {
            if (analyzers.indexOf(na) == position){
                return na.name();
            }
        }
        return "analyzer not found";
    }
}

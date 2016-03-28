package com.ghostofchaos.especialdish.Fragments;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.MyPagerAdapter;
import com.ghostofchaos.especialdish.R;

/**
 * Created by Ghost on 25.02.2016.
 */
public class FragmentSearch extends Fragment {

    ProgressBar progressBar;
    Typeface typeface;
    boolean refresh;
    int page;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search, null);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getContext());

        setMenu();

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) root.findViewById(R.id.tabs);
        tabs.setShouldExpand(true);
        tabs.setViewPager(pager);
        tabs.setTypeface(typeface, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tabs.setBackground(getResources().getDrawable(R.color.colorPrimary));
        }
        tabs.setTextColor(Color.parseColor("#FFFFFF"));
        tabs.setIndicatorColor(Color.parseColor("#FFFFFF"));

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        MenuItem search = menu.add("Search");
        search.setIcon(R.drawable.vector_search);
        search.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        MenuItem tune = menu.add("Tune");
        tune.setIcon(R.drawable.vector_tune);
        tune.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        CharSequence title = item.getTitle();

        if (title.equals("Search")) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void setMenu() {
        setHasOptionsMenu(true);
    }
}

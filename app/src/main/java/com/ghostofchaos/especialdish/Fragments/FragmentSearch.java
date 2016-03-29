package com.ghostofchaos.especialdish.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.Adapter.TabAdapter;
import com.ghostofchaos.especialdish.R;

/**
 * Created by Ghost on 25.02.2016.
 */
public class FragmentSearch extends Fragment {

    ProgressBar progressBar;
    Typeface typeface;
    FrameLayout frameLayout;
    View shadow;
    boolean refresh;
    int page;
    private boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search, null);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.main_frame);
        shadow = getActivity().findViewById(R.id.toolbar_shadow);
        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getContext());

        setMenu();

        //frameLayout.removeView(getActivity().findViewById(R.id.toolbar_shadow));
        shadow.setVisibility(View.INVISIBLE);

        final ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
        pager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);

        for (int i = 0; i < tabAdapter.getCount(); i++) {
            TextView t = new TextView(getContext());
            t.setText(tabAdapter.getPageTitle(i) );
            t.setTypeface(typeface);
            t.setTextColor(Color.parseColor("#FFFFFF"));
            t.setAllCaps(true);
            t.setGravity(Gravity.CENTER);

            tabLayout.addTab(tabLayout.newTab()
                    .setCustomView(t));
        }

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

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

    @Override
    public void onStop() {
        super.onStop();
        shadow.setVisibility(View.VISIBLE);
    }
}

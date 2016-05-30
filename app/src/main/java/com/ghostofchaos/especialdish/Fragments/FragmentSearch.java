package com.ghostofchaos.especialdish.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ghostofchaos.especialdish.Adapter.SearchRestaurantsListAdapter;
import com.ghostofchaos.especialdish.Adresses;
import com.ghostofchaos.especialdish.DownloadObjectsManager;
import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.Adapter.TabAdapter;
import com.ghostofchaos.especialdish.MainActivity;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.ghostofchaos.especialdish.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Ghost on 25.02.2016.
 */
public class FragmentSearch extends Fragment {

    ArrayList<Object> restaurantsModelList;
    ListView listView;
    ProgressBar progressBar;
    Typeface typeface;
    FrameLayout frameLayout;
    View shadow;
    String host;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search, null);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.main_frame);
        shadow = getActivity().findViewById(R.id.toolbar_shadow);
        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getContext());
        restaurantsModelList = new ArrayList<>();
        listView = (ListView) root.findViewById(R.id.list);

        setMenu();

        setHost();

        shadow.setVisibility(View.INVISIBLE);

        final ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        TabAdapter tabAdapter = new TabAdapter(getChildFragmentManager());
        pager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tab_layout);

        for (int i = 0; i < tabAdapter.getCount(); i++) {
            TextView t = new TextView(getContext());
            t.setText(tabAdapter.getPageTitle(i));
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

    private void setHost() {
        host = Adresses.GET_RESTAURANTS + Adresses.PAGE;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DownloadObjectsManager.getInstance();
                DownloadObjectsManager.setModelArrayList(restaurantsModelList);
                DownloadObjectsManager.setProgressBar(progressBar);
                DownloadObjectsManager.setKeywords(query);
                DownloadObjectsManager.setListView(listView);
                DownloadObjectsManager.refresh = true;
                DownloadObjectsManager.setPage(0);
                DownloadObjectsManager.setHost(Adresses.GET_RESTAURANTS + Adresses.PAGE);
                DownloadObjectsManager.downloadObjects(getContext(), FragmentMap.map, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                DownloadObjectsManager.getInstance();
                DownloadObjectsManager.setModelArrayList(restaurantsModelList);
                DownloadObjectsManager.setProgressBar(progressBar);
                DownloadObjectsManager.setKeywords("");
                DownloadObjectsManager.setListView(listView);
                DownloadObjectsManager.refresh = true;
                DownloadObjectsManager.setPage(0);
                DownloadObjectsManager.setHost(Adresses.GET_RESTAURANTS + Adresses.PAGE);
                DownloadObjectsManager.downloadObjects(getContext(), FragmentMap.map, false);
                return false;
            }
        });

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

        /*FragmentRestaurants.page = 1;
        FragmentRestaurants.refresh = true;
        FragmentRestaurants.keywords = "";
        if (FragmentRestaurants.restaurantsModelList != null) {
            FragmentRestaurants.restaurantsModelList.clear();
            FragmentRestaurants.searchRestaurantsListAdapter.notifyDataSetChanged();
            FragmentRestaurants.progressBar.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        shadow.setVisibility(View.INVISIBLE);

        DownloadObjectsManager.downloadObjects(FragmentMap.map, getActivity(), RestaurantsModel.class);
        /*FragmentRestaurants.setHost();
        FragmentRestaurants.loadObjects(getContext());*/
    }
}

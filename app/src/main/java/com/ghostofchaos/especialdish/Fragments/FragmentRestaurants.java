package com.ghostofchaos.especialdish.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.ghostofchaos.especialdish.Adapter.SearchRestaurantsListAdapter;
import com.ghostofchaos.especialdish.DownloadObjectsManager;
import com.ghostofchaos.especialdish.MainActivity;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.ghostofchaos.especialdish.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Ghost on 28.03.2016.
 */
public class FragmentRestaurants extends Fragment {

    static StringRequest stringRequest;
    static ArrayList<Object> restaurantsModelList;
    static String host;
    static SearchRestaurantsListAdapter searchRestaurantsListAdapter;
    static ListView listView;
    static SwipeRefreshLayout swipeRefreshLayout;
    static ProgressBar progressBar;
    static View footer;
    TextView tvToolbar;
    String toolbarTitle;
    //static boolean refresh;
    //static boolean loading = true;
    static int page;
    static int perPage;
    static ArrayList<Object> list;
    static String keywords = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        tvToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        footer = View.inflate(getActivity(), R.layout.pagination_footer, null);
        footer.setVisibility(View.GONE);
        restaurantsModelList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary));
        listView = (ListView) root.findViewById(R.id.list);
        listView.addFooterView(footer, null, false);

        setPage();

        setToolbar();

        setHost();


        searchRestaurantsListAdapter = new SearchRestaurantsListAdapter(getActivity(), R.layout.restaurants_item, restaurantsModelList, toolbarTitle);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(searchRestaurantsListAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        setManager();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                Boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                if (MainActivity.loading) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        MainActivity.loading = false;
                        if (page > 1) {
                            footer.setVisibility(View.VISIBLE);
                        }
                        downloadObjects();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setPage();
                swipeRefreshLayout.setRefreshing(true);
                MainActivity.refresh = true;
                downloadObjects();
            }
        });

        return root;
    }

    public void setPage() {
        page = 1;
    }

    public void setHost() {
        host = "http://osoboebludo.com/api/?notabs&json&content_id=16&page=";
    }

    public void setToolbar() {
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.search) + "</b>"));
        toolbarTitle = getResources().getString(R.string.search);
    }

    public void setList(String s) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jObject = parser.parse(s).getAsJsonObject();
        JsonElement jElement = jObject.get("items");
        JsonArray jArray = jElement.getAsJsonArray();
        Type type = new TypeToken<ArrayList<RestaurantsModel>>() {
        }.getType();
        list = gson.fromJson(jArray, type);
    }

    public void setPerPage(int count) {
        perPage = count;
    }

    private void setManager() {
        //DownloadObjectsManager.setRefresh(refresh);
        //DownloadObjectsManager.setLoading(loading);
        DownloadObjectsManager.getInstance();
        DownloadObjectsManager.setModelArrayList(restaurantsModelList);
        DownloadObjectsManager.setSwipeRefreshLayout(swipeRefreshLayout);
        DownloadObjectsManager.setProgressBar(progressBar);
        DownloadObjectsManager.setKeywords(keywords);
        DownloadObjectsManager.setFooter(footer);
        DownloadObjectsManager.setListView(listView);
        DownloadObjectsManager.setListAdapter(searchRestaurantsListAdapter);
        DownloadObjectsManager.setPage(0);
        DownloadObjectsManager.setHost("http://osoboebludo.com/api/?notabs&json&content_id=16&page=");
    }

    public void downloadObjects() {

        DownloadObjectsManager.downloadObjects(getActivity(), FragmentMap.map);

    }
}

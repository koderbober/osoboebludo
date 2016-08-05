package com.ghostofchaos.especialdish.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ghostofchaos.especialdish.Adapter.SearchRestaurantsListAdapter;
import com.ghostofchaos.especialdish.Adresses;
import com.ghostofchaos.especialdish.DownloadObjectsManager;
import com.ghostofchaos.especialdish.MainActivity;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.ghostofchaos.especialdish.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Ghost on 28.03.2016.
 */
public class FragmentRestaurants extends Fragment {

    static ArrayList<Object> restaurantsModelList;
    static ArrayList<Object> restaurantsStaticModelList;
    String host;
    static SearchRestaurantsListAdapter searchRestaurantsListAdapter;
    static ListView listView;
    static SwipeRefreshLayout swipeRefreshLayout;
    static ProgressBar progressBar;
    static View footer;
    TextView tvToolbar;
    String toolbarTitle;
    static int page;
    static int modelsCount = 0;
    static String keywords = "";
    static boolean refresh;
    static boolean loading = true;
    static Context context;

    private static int MODELS_COUNT_PER_PAGE = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        tvToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        footer = View.inflate(getActivity(), R.layout.pagination_footer, null);
        footer.setVisibility(View.GONE);
        context = getActivity();
        restaurantsModelList = new ArrayList<>();
        restaurantsStaticModelList = new ArrayList<>();
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

        searchRestaurantsListAdapter = new SearchRestaurantsListAdapter(
                getActivity(),
                R.layout.restaurants_item,
                restaurantsModelList,
                toolbarTitle);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(searchRestaurantsListAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

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
                if (loading) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        loading = false;
                        page = DownloadObjectsManager.page;
                        if (page > 1) {
                            footer.setVisibility(View.VISIBLE);
                        }
                        loadObjects(false);
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
                refresh = true;
                clear();
                loadAll();
                loadObjects(false);
                FragmentSearch.searchView.onActionViewCollapsed();
            }
        });

        return root;
    }

    public void setPage() {
        page = 1;
    }

    public void setHost() {
        host = Adresses.GET_RESTAURANTS + Adresses.PAGE;
    }

    public void setToolbar() {
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.search) + "</b>"));
        toolbarTitle = getResources().getString(R.string.search);
    }

    private void setManager() {
        DownloadObjectsManager.refresh = true;
        DownloadObjectsManager.getInstance();
        DownloadObjectsManager.setModelArrayList(restaurantsModelList);
        DownloadObjectsManager.setSwipeRefreshLayout(swipeRefreshLayout);
        DownloadObjectsManager.setProgressBar(progressBar);
        DownloadObjectsManager.setKeywords(keywords);
        DownloadObjectsManager.setFooter(footer);
        DownloadObjectsManager.setListView(listView);
        DownloadObjectsManager.setListAdapter(searchRestaurantsListAdapter);
        DownloadObjectsManager.setPage(0);
        DownloadObjectsManager.setPerPage(10);
        DownloadObjectsManager.setHost(Adresses.GET_RESTAURANTS + Adresses.PAGE);
    }

    public static void loadObjects(boolean search) {

        if (search) {
            searchRestaurants();
        }
        for (int i = 0; i < MODELS_COUNT_PER_PAGE; i++) {
            try {
                restaurantsModelList.add(restaurantsStaticModelList.get(modelsCount));
                modelsCount++;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                listView.removeFooterView(footer);
                loading = false;
                if (FragmentMap.map != null) {
                    addMarkers();
                }
                return;
            }
        }
        if (FragmentMap.map != null) {
            addMarkers();
        }
        if (searchRestaurantsListAdapter != null) {
            searchRestaurantsListAdapter.notifyDataSetChanged();
            Log.d("listadapter", searchRestaurantsListAdapter.getCount() + "");
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }
        loading = true;
        page++;

        if (progressBar != null)
            progressBar.setVisibility(View.GONE);

        if (footer != null)
            footer.setVisibility(View.GONE);

        refresh = false;
    }

    private static void addMarkers() {
        for (Object object : restaurantsModelList) {
            final RestaurantsModel model = (RestaurantsModel) object;
            final LatLng[] loc = new LatLng[1];
            final String title = model.getTitle();
            if (model.getLocation() != null) {
                loc[0] = model.getLocation().getLatLng();
            } else {
                String address = model.getAddress();
                loc[0] = DownloadObjectsManager.getLocationFromAddress(context, address);
            }
            if (loc[0] != null) {
                FragmentMap.map.addMarker(new MarkerOptions()
                        .position(loc[0])
                        .title(title))
                        .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant_icon));
            }
        }
    }

    public static void searchRestaurants() {

        clear();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<RestaurantsModel> results = realm
                .where(RestaurantsModel.class)
                .contains("search_all", keywords, Case.INSENSITIVE)
                .findAll();
        Log.i("query", results.toString());

        for (RestaurantsModel model : results) {
            restaurantsStaticModelList.add(model);
        }
        realm.commitTransaction();

        if (searchRestaurantsListAdapter != null) {
            searchRestaurantsListAdapter.notifyDataSetChanged();
            Log.d("listadapter", searchRestaurantsListAdapter.getCount() + "");
        }
    }

    public static void loadAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<RestaurantsModel> results = realm.where(RestaurantsModel.class).findAll();
        for (RestaurantsModel model : results) {
            restaurantsStaticModelList.add(model);
        }
        realm.commitTransaction();
    }

    private static void clear() {
        restaurantsStaticModelList.clear();
        restaurantsModelList.clear();
        modelsCount = 0;
        FragmentMap.map.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        loadAll();
        loadObjects(false);
    }
}

package com.ghostofchaos.especialdish.Fragments;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Ghost on 28.03.2016.
 */
public class FragmentRestaurants extends Fragment {

    ArrayList<Object> restaurantsModelList;
    static ArrayList<Object> restaurantsStaticModelList;
    String host;
    SearchRestaurantsListAdapter searchRestaurantsListAdapter;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    View footer;
    TextView tvToolbar;
    String toolbarTitle;
    int page;
    int modelsCount = 0;
    String keywords = "";
    boolean refresh;
    boolean loading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        tvToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        footer = View.inflate(getActivity(), R.layout.pagination_footer, null);
        footer.setVisibility(View.GONE);
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

        //setManager();

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
                        loadObjects();
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
                DownloadObjectsManager.refresh = true;
                loadObjects();
            }
        });

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<RestaurantsModel> results = realm.where(RestaurantsModel.class).findAll();
        for (RestaurantsModel model : results) {
            restaurantsStaticModelList.add(model);
        }
        realm.commitTransaction();

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

    public void loadObjects() {

        //DownloadObjectsManager.loadObjects(getActivity(), FragmentMap.map, false);
        //DownloadObjectsManager.loadObjects(FragmentMap.map, getActivity(), RestaurantsModel.class);

        if (refresh) {
            restaurantsModelList.clear();
        }

        for (int i = 0; i < 10; i++) {
            restaurantsModelList.add(restaurantsStaticModelList.get(modelsCount));
            modelsCount++;
        }

        if (false/*s.equals("")*/) {
            listView.removeFooterView(footer);
            loading = false;

        } else {
            if (FragmentMap.map != null) {
                for (Object object : restaurantsModelList) {
                    final RestaurantsModel model = (RestaurantsModel) object;
                    final LatLng[] loc = new LatLng[1];
                    final String title = model.getTitle();
                    if (model.getLocation() != null) {
                        loc[0] = model.getLocation().getLatLng(); //DownloadObjectsManager.getLocationFromAddress(getActivity(), address);
                    }
                    if (loc[0] != null) {
                        FragmentMap.map.addMarker(new MarkerOptions()
                                .position(loc[0])
                                .title(title));
                    }
                }
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
    }
}

package com.ghostofchaos.especialdish.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ghostofchaos.especialdish.Adapter.SearchRestaurantsListAdapter;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.ghostofchaos.especialdish.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Ghost on 28.03.2016.
 */
public class FragmentRestaurants extends Fragment {

    static StringRequest stringRequest;
    static ArrayList<RestaurantsModel> restaurantsModelList;
    static String host;
    static SearchRestaurantsListAdapter searchRestaurantsListAdapter;
    static ListView listView;
    static SwipeRefreshLayout swipeRefreshLayout;
    static ProgressBar progressBar;
    static View footer;
    TextView tvToolbar;
    String toolbarTitle;
    static public boolean isMap = false;
    static boolean refresh;
    static int page;
    static boolean loading = true;
    static ArrayList<RestaurantsModel> list;
    static String keywords = "";
    static int perPage;

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
                        if (page > 1) {
                            footer.setVisibility(View.VISIBLE);
                        }
                        downloadObjects(getContext());
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
                downloadObjects(getContext());
            }
        });


        return root;
    }

    public void setPage() {
        page = 1;
    }

    public static void setHost() {
        host = "http://osoboebludo.com/api/?notabs&json&content_id=16&page=";
    }

    public void setToolbar() {
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.search) + "</b>"));
        toolbarTitle = getResources().getString(R.string.search);
    }

    public static void setList(String s) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jObject = parser.parse(s).getAsJsonObject();
        JsonElement jElement = jObject.get("items");
        JsonArray jArray = jElement.getAsJsonArray();
        //JsonArray jArray = parser.parse(s).getAsJsonArray();
        Type type = new TypeToken<ArrayList<RestaurantsModel>>() {
        }.getType();
        list = gson.fromJson(jArray, type);
    }

    public static void setPerPage(int count) {
        perPage = count;
    }

    public static void downloadObjects(final Context context) {
        //Activity activity = (Activity) listView.getContext();
        setPerPage(10);
        RequestQueue queue = Volley.newRequestQueue(context);
        String query = null;
        try {
            query = URLEncoder.encode(keywords, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringRequest = new StringRequest(Request.Method.GET, host + page + "&keywords=" + query + "&per_page=" + perPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (refresh) {
                            restaurantsModelList.clear();
                        }
                        if (s.equals("")) {
                            listView.removeFooterView(footer);
                            loading = false;
                        } else {
                            Log.d("stringRequest", s);
                            Log.d("address", host + page);
                            setList(s);
                            restaurantsModelList.addAll(list);
                            Log.d("list", restaurantsModelList.size() + "");
                            searchRestaurantsListAdapter.notifyDataSetChanged();
                            Log.d("listadapter", searchRestaurantsListAdapter.getCount() + "");
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setEnabled(false);
                            loading = true;
                            page++;
                            progressBar.setVisibility(View.GONE);
                            footer.setVisibility(View.GONE);
                            refresh = false;
                            if (isMap) {
                                FragmentMap.setMarkers();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("news", volleyError.toString());
                        Toast.makeText(context, "При загрузке данных произошла ошибка", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setEnabled(false);
                        loading = true;
                    }
                }) {
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}

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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ghostofchaos.especialdish.Adapter.AdapterList;
import com.ghostofchaos.especialdish.Objects.FeedModel;
import com.ghostofchaos.especialdish.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

    StringRequest stringRequest;
    ArrayList<FeedModel> feedModelList;
    String host;
    AdapterList adapterList;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    View footer;
    TextView tvToolbar;
    String toolbarTitle;
    boolean refresh;
    int page;
    boolean loading = true;
    ArrayList<FeedModel> list;
    String keywords = "кофе";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list, null);
        tvToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        footer = View.inflate(getActivity(), R.layout.pagination_footer, null);
        footer.setVisibility(View.GONE);
        feedModelList = new ArrayList<>();
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

        adapterList = new AdapterList(getActivity(), R.layout.item_card, feedModelList, toolbarTitle);
        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapterList);
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
                Log.i("TopRowVerticalPosition", topRowVerticalPosition + "");
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                Boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                Log.i("IsRefreshing", isRefreshing.toString());
                if (loading) {
                    if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                        loading = false;
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
                refresh = true;
                downloadObjects();
            }
        });


        return root;
    }

    public void setPage() {
        page = 1;
    }

    public void setHost() {
        host = "http://osoboebludo.com/api/?notabs&json&content_id=1&page=";
    }

    public void setToolbar() {
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.search) + "</b>"));
        toolbarTitle = getResources().getString(R.string.search);
    }

    public void setList(String s) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(s).getAsJsonArray();
        Type type = new TypeToken<ArrayList<FeedModel>>() {
        }.getType();
        list = gson.fromJson(jArray, type);
    }

    public void downloadObjects() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String query = null;
        try {
            query = URLEncoder.encode(keywords, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringRequest = new StringRequest(Request.Method.GET, host + page + "&keywords=" + query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (refresh) {
                            feedModelList.clear();
                        }
                        if (s.equals("")) {
                            listView.removeFooterView(footer);
                            loading = false;
                        } else {
                            Log.d("stringRequest", s);
                            Log.d("address", host + page);
                            setList(s);
                            feedModelList.addAll(list);
                            Log.d("list", feedModelList.size() + "");
                            adapterList.notifyDataSetChanged();
                            Log.d("listadapter", adapterList.getCount() + "");
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setEnabled(false);
                            loading = true;
                            page++;
                            progressBar.setVisibility(View.GONE);
                            footer.setVisibility(View.GONE);
                            refresh = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("news", volleyError.toString());
                        Toast.makeText(getActivity(), "При загрузке данных произошла ошибка", Toast.LENGTH_SHORT).show();
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

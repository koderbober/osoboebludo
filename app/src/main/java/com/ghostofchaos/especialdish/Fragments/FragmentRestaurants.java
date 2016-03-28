package com.ghostofchaos.especialdish.Fragments;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ghostofchaos.especialdish.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Ghost on 28.03.2016.
 */
public class FragmentRestaurants extends FragmentMain {

    String keywords = "кофе";

    @Override
    public void setHost() {
        super.setHost();
        host = "http://osoboebludo.com/api/?notabs&json&content_id=1&page=";
    }

    @Override
    public void setToolbar() {
        super.setToolbar();
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.search) + "</b>"));
        toolbarTitle = getResources().getString(R.string.search);
    }

    @Override
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
                            modelList.clear();
                        }
                        if (s.equals("")) {
                            listView.removeFooterView(footer);
                            loading = false;
                        } else {
                            Log.d("stringRequest", s);
                            Log.d("address", host + page);
                            setList(s);
                            modelList.addAll(list);
                            Log.d("list", modelList.size() + "");
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

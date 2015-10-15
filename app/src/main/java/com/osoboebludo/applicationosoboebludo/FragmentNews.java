package com.osoboebludo.applicationosoboebludo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentNews extends Fragment {

    int page = 1;
    final String ATTRIBUTE_TITLE = "title";
    final String ATTRIBUTE_INTRO = "intro";
    final String ATTRIBUTE_ICON = "icon";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        final ListView listView = (ListView) rootView.findViewById(R.id.list);

        final List<News> newsList = getNewsList(page);
        final ArrayList<Map<String, Object>> data = new ArrayList<>(newsList.size());
        for (News each : newsList) {
            FragmentBitmap fragmentBitmap = new FragmentBitmap();
            fragmentBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = fragmentBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getLargePhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            data.add(map);
        }

        String[] from = {ATTRIBUTE_TITLE, ATTRIBUTE_INTRO, ATTRIBUTE_ICON};
        int[] to = {R.id.tvTextTitle, R.id.tvTextIntro, R.id.ivImg};

        final SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), data, R.layout.item_cards, from, to);
        simpleAdapter.setViewBinder(new ViewBinderActivity());
        listView.setAdapter(simpleAdapter);
        registerForContextMenu(listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    page++;
                    addData(data);
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = newsList.get(position);
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra("NEWS", news);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private List<News> getNewsList(int page) {
        String url = "http://osoboebludo.com/api/?notabs&json&content_id=1&page=" + page;
        String json = Utils.getPageJson(url);

        List<News> newsList = new ArrayList<>();
        try {
            JSONArray newsJSONArray = new JSONArray(json);
            for (int i = 0; i < newsJSONArray.length(); i++) {
                JSONObject newsJSONObject = (JSONObject) newsJSONArray.get(i);

                News news = new News();
                news.setTitle(newsJSONObject.getString("title"));
                news.setIntro(newsJSONObject.getString("intro"));
                if (newsJSONObject.isNull("image")) {
                    newsJSONObject.put("image", " ");
                }
                news.setLargePhoto(newsJSONObject.getString("image"));
                news.setId(newsJSONObject.getString("id"));
                news.setDescription(newsJSONObject.getString("description"));
                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    private ArrayList<Map<String, Object>> addData(ArrayList<Map<String, Object>> data) {
        List<News> newsList = getNewsList(page);
        for (News each : newsList) {
            FragmentBitmap fragmentBitmap = new FragmentBitmap();
            fragmentBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = fragmentBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getLargePhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            data.add(map);
        }
        return data;
    }
}
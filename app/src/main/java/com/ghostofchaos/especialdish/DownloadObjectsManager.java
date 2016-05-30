package com.ghostofchaos.especialdish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ghostofchaos.especialdish.Objects.RestaurantsLocation;
import com.ghostofchaos.especialdish.Objects.RestaurantsModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Ghost on 23.05.2016.
 */
public class DownloadObjectsManager {


    static StringRequest stringRequest;
    static ArrayList<Object> modelArrayList;
    static String host;
    static ArrayAdapter arrayAdapter;
    static ListView listView;
    static SwipeRefreshLayout swipeRefreshLayout;
    static ProgressBar progressBar;
    static View footer;
    public static boolean refresh;
    public static boolean loading = true;
    public static int page;
    static ArrayList<Object> list;
    static String keywords = "";
    static int perPage;
    private static DownloadObjectsManager instance;
    static SharedPreferences sPref;

    private DownloadObjectsManager() {
    }

    public static DownloadObjectsManager getInstance() {
        if (instance == null) {
            instance = new DownloadObjectsManager();
        }
        return instance;
    }

    public static ArrayList<Object> getList() {
        return list;
    }

    public static void setList(String s) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jObject = parser.parse(s).getAsJsonObject();
        JsonElement jElement = jObject.get("items");
        JsonArray jArray = jElement.getAsJsonArray();
        Type type = new TypeToken<ArrayList<RestaurantsModel>>() {
        }.getType();
        list = gson.fromJson(jArray, type);
    }

    public static void setModelArrayList(ArrayList<Object> modelArrayList) {
        DownloadObjectsManager.modelArrayList = modelArrayList;
    }

    public static void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        DownloadObjectsManager.swipeRefreshLayout = swipeRefreshLayout;
    }

    public static void setProgressBar(ProgressBar progressBar) {
        DownloadObjectsManager.progressBar = progressBar;
    }

    public static void setKeywords(String s) {
        keywords = s;
    }

    public static void setFooter(View v) {
        footer = v;
    }

    public static void setPerPage(int count) {
        perPage = count;
    }

    public static void setListView(ListView lv) {
        listView = lv;
    }

    public static void setListAdapter(ArrayAdapter adapter) {
        arrayAdapter = adapter;
    }

    public static void setHost(String url) {
        host = url;
    }

    public static void setPage(int p) {
        page = p;
        if (page == 0) {
            page = 1;
        }
    }

    public static void downloadObjects(final Context context, final GoogleMap map, final boolean saveToRealm) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String query = null;
        try {
            query = URLEncoder.encode(keywords, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        stringRequest = new StringRequest(Request.Method.GET, host + page + Adresses.KEYWORDS + query + Adresses.PER_PAGE + perPage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (refresh) {
                            modelArrayList.clear();
                        }

                        if (s.equals("")) {
                            listView.removeFooterView(footer);
                            loading = false;

                        } else {
                            Log.d("stringRequest", s);
                            Log.d("address", host + page);
                            setList(s);

                            if (list != null) {
                                modelArrayList.addAll(list);
                                Log.d("list", modelArrayList.size() + "");
                                Log.d("class", list.get(0).getClass().toString());

                                if (saveToRealm) {
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();

                                    if (list.get(0).getClass() == RestaurantsModel.class) {
                                        for (Object model : list) {
                                            //((RestaurantsModel) model).setLocation(getLocationFromAddress(context, ((RestaurantsModel) model).getAddress()));
                                            RestaurantsLocation location = new RestaurantsLocation();
                                            LatLng latLng = getLocationFromAddress(context, ((RestaurantsModel) model).getAddress());
                                            if (latLng != null) {
                                                location.setLatitude(latLng.latitude);
                                                location.setLongitude(latLng.longitude);
                                                ((RestaurantsModel) model).setLocation(location);
                                            }
                                            realm.copyToRealmOrUpdate((RestaurantsModel) model);
                                            //setMarkers(map, context);
                                        }
                                    }

                                    /*if (list.get(0).getClass() == DishesModel.class) {
                                        realm.createOrUpdateAllFromJson(DishesModel.class, s);
                                    }*/

                                    realm.commitTransaction();
                                }
                            }

                            if (arrayAdapter != null) {
                                arrayAdapter.notifyDataSetChanged();
                                Log.d("listadapter", arrayAdapter.getCount() + "");
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

                            sPref = getSharedPreferences(context);
                            boolean isFirst = sPref.getBoolean("firstStart", false);
                            //if (isFirst) {
                            if (true) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }

                            refresh = false;
                            if (map != null) {
                                //setMarkers(map, context);
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

    public static void downloadObjects(GoogleMap map, Context context, Class<? extends RealmObject> cls) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (refresh) {
            modelArrayList.clear();
            list.clear();
        }

        if (cls == RestaurantsModel.class) {
            RealmResults<RestaurantsModel> results = realm.where(RestaurantsModel.class).findAll();
            for (RestaurantsModel model : results) {
                modelArrayList.add(model);
            }
        }

        if (false/*s.equals("")*/) {
            listView.removeFooterView(footer);
            loading = false;

        } else {
            if (map != null) {
                if (cls == RestaurantsModel.class) {
                    for (Object object : modelArrayList) {
                        final RestaurantsModel model = (RestaurantsModel) object;
                        LatLng loc = null;
                        if (model.getAddress() != null) {
                            loc = getLocationFromAddress(context, model.getAddress());
                        }
                        if (loc != null) {
                            map.addMarker(new MarkerOptions()
                                    .position(loc)
                                    .title(model.getTitle()));
                        }
                    }
                }
            }

            /*if (list.get(0).getClass() == DishesModel.class) {
                realm.createOrUpdateAllFromJson(DishesModel.class, s);
            }*/

            realm.commitTransaction();

            if (arrayAdapter != null) {
                arrayAdapter.notifyDataSetChanged();
                Log.d("listadapter", arrayAdapter.getCount() + "");
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

    public static void setMarkers(final GoogleMap map, final Context context) {
        new Thread(new Runnable() {
            public void run() {
                for (Object object : modelArrayList) {
                    final RestaurantsModel model = (RestaurantsModel) object;
                    LatLng loc = null;
                    if (model.getAddress() != null) {
                        loc = getLocationFromAddress(context, model.getAddress());
                    }
                    if (loc != null) {
                        addMarkerOnNewThread(map, loc, model.getTitle());
                    }
                }
            }
        }).start();
    }

    public static void addMarkerOnNewThread(final GoogleMap map, LatLng loc, final String title) {
        final LatLng finalLoc = loc;
        MainActivity.UIHandler.post(new Runnable() {
            @Override
            public void run() {
                map.addMarker(new MarkerOptions()
                        .position(finalLoc)
                        .title(title));
            }
        });
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        Address location;

        try {
            address = coder.getFromLocationName(strAddress, 2);
            if (address == null) {
                return null;
            }
            if (address.size() > 0) {
                location = address.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latLng;
    }

    public static SharedPreferences getSharedPreferences(Context ctxt) {
        return ctxt.getSharedPreferences("sPref", Context.MODE_PRIVATE);
    }
}

package com.osoboebludo.applicationosoboebludo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ActionBarDrawerToggle myDrawerToggle;

    // navigation drawer title
    private CharSequence myDrawerTitle;
    // used to store app title
    private CharSequence myTitle;

    private String[] viewsNames;
    private Fragment fragment;

    private int pageRestaurants = 1;
    private int pageDishes = 1;

    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<Map<String, Object>> data;

    String searchText;
    final String ATTRIBUTE_TITLE = "title";
    final String ATTRIBUTE_INTRO = "intro";
    final String ATTRIBUTE_ICON = "image";
    final String ATTRIBUTE_RESTAURANT_NAME = "restaurants";
    final String ATTRIBUTE_RESTAURANT_ADDRESS = "restaurants_address";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTitle = getTitle();
        myDrawerTitle = getResources().getString(R.string.menu);

        // load slide menu items
        viewsNames = getResources().getStringArray(R.array.views_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);

        myDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, viewsNames));

        // enabling action bar app icon and behaving it as toggle button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        ImageView imageTestView = (ImageView) findViewById(R.id.testView);
//        imageTestView.setImageBitmap(Utils.loadBitmapTask("http://osoboebludo.com/uploads/content/foto_73_312.jpg"));

        myDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,
                R.string.open_menu,
                R.string.close_menu
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(myTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(myDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        myDrawerLayout.setDrawerListener(myDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        myDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentNews();
                break;
            case 1:
                fragment = new FragmentProjects();
                break;
            case 2:
                fragment = new FragmentReviews();
                break;
            case 3:
                fragment = new FragmentBlogs();
                break;
            case 4:
                fragment = new FragmentCalendar();
                break;
            case 5:
                fragment = new FragmentAbout();
                break;
            case 6:
                fragment = new FragmentHelp();
                break;
            default:
                break;
        }

        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_linear, fragment).commit();

            // update selected item and title, then close the drawer
            myDrawerList.setItemChecked(position, true);
            myDrawerList.setSelection(position);
            setTitle(viewsNames[position]);
            myDrawerLayout.closeDrawer(myDrawerList);

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // toggle nav drawer on selecting action bar app icon/title
        if (myDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    /*public boolean onPrepareOptionsMenu(Menu menu) {
        // if navigation drawer is opened, hide the action items
        boolean drawerOpen = myDrawerLayout.isDrawerOpen(myDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    public void setTitle(CharSequence title) {
        myTitle = title;
        getSupportActionBar().setTitle(myTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        myDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Click to RestaurantButton for return to search results
    public void searchRestaurantButtonClick(View view) {

        EditText searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchText = searchEditText.getText().toString();

        listView = (ListView) findViewById(R.id.list);
        final List<Restaurant> restaurantsList = getRestaurantsList(searchText);

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

        data = new ArrayList<>(restaurantsList.size());
        for (Restaurant each : restaurantsList) {
            RestaurantBitmap restaurantBitmap = new RestaurantBitmap();
            restaurantBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = restaurantBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getLargePhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            map.put(
                    ATTRIBUTE_RESTAURANT_ADDRESS,
                    getResources().getString(R.string.restaurantsAddressTemplate) + " " + each.getCity() + ", " + each.getAddress()
            );
            data.add(map);
        }

        String[] from = {ATTRIBUTE_TITLE, ATTRIBUTE_INTRO, ATTRIBUTE_ICON, ATTRIBUTE_RESTAURANT_ADDRESS};
        int[] to = {R.id.tvTextTitle, R.id.tvTextIntro, R.id.ivImg, R.id.tvTextAddress};

        simpleAdapter = new SimpleAdapter(getApplicationContext(), data, R.layout.item_search_restaurants, from, to);
        simpleAdapter.setViewBinder(new ViewBinderActivity());
        listView.setAdapter(simpleAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    pageRestaurants++;
                    AddDataToRestaurantsSimpleAdapter adtrsa = new AddDataToRestaurantsSimpleAdapter();
                    adtrsa.execute();
                    //addDataToRestaurantsSimpleAdapter(data, searchText);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = restaurantsList.get(position);
                Intent intent = new Intent(MainActivity.this, RestaurantActivity.class);
                intent.putExtra("RESTAURANT", restaurant);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Map<String, Object>> addDataToRestaurantsSimpleAdapter(ArrayList<Map<String, Object>> data, String searchText) {
        List<Restaurant> restaurantsList = getRestaurantsList(searchText, pageRestaurants);
        for (Restaurant each : restaurantsList) {
            RestaurantBitmap restaurantBitmap = new RestaurantBitmap();
            restaurantBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = restaurantBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getLargePhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            map.put(
                    ATTRIBUTE_RESTAURANT_ADDRESS,
                    getResources().getString(R.string.restaurantsAddressTemplate) + " " + each.getCity() + ", " + each.getAddress()
            );
            data.add(map);
        }
        simpleAdapter.notifyDataSetChanged();
        return data;
    }

    public void searchDishesButtonClick(View view) {

        EditText searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchText = searchEditText.getText().toString();

        listView = (ListView) findViewById(R.id.list);
        final List<Dish> dishesList = getDishesList(searchText);

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

        final ArrayList<Map<String, Object>> data = new ArrayList<>(dishesList.size());
        for (Dish each : dishesList) {
            DishBitmap dishBitmap = new DishBitmap();
            dishBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = dishBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getSmallPhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            map.put(ATTRIBUTE_RESTAURANT_ADDRESS, getResources().getString(R.string.restaurantsAddressTemplate) + " " + each.getRestaurantsAddress());
            map.put(ATTRIBUTE_RESTAURANT_NAME, getResources().getString(R.string.restaurantsNameTemplate) + " " + each.getRestaurantsName());
            data.add(map);
        }

        String[] from = {ATTRIBUTE_TITLE, ATTRIBUTE_ICON, ATTRIBUTE_RESTAURANT_ADDRESS, ATTRIBUTE_RESTAURANT_NAME};
        int[] to = {R.id.tvTextTitle, R.id.ivImg, R.id.tvRestaurantsAddress, R.id.tvRestaurantsName};

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.item_search_dishes, from, to);
        simpleAdapter.setViewBinder(new ViewBinderActivity());
        listView.setAdapter(simpleAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    pageRestaurants++;
                    addDataToDishesSimpleAdapter(data, searchText);
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dish dish = dishesList.get(position);
                Intent intent = new Intent(MainActivity.this, DishActivity.class);
                intent.putExtra("DISH", dish);
                startActivity(intent);
            }
        });
    }

    private ArrayList<Map<String, Object>> addDataToDishesSimpleAdapter(ArrayList<Map<String, Object>> data, String searchText) {
        List<Dish> dishesList = getDishesList(searchText, pageDishes);
        for (Dish each : dishesList) {
            DishBitmap dishBitmap = new DishBitmap();
            dishBitmap.setBitmapLargePhoto(each.getLargePhoto());
            Bitmap bitmap = dishBitmap.getBitmapLargePhoto();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_TITLE, each.getTitle());
            map.put(ATTRIBUTE_INTRO, each.getIntro());
            if (each.getSmallPhoto() != " ") {
                map.put(ATTRIBUTE_ICON, bitmap);
            }
            map.put(ATTRIBUTE_RESTAURANT_ADDRESS, getResources().getString(R.string.restaurantsAddressTemplate) + " " + each.getRestaurantsAddress());
            map.put(ATTRIBUTE_RESTAURANT_NAME, getResources().getString(R.string.restaurantsNameTemplate) + " " + each.getRestaurantsName());
            data.add(map);
        }
        return data;
    }

    private List<Restaurant> getRestaurantsList(String searchText) {
        try {
            searchText = URLEncoder.encode(searchText, "UTF-8").replaceAll(" ", "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://osoboebludo.com/api/?notabs&json&content_id=16&keywords=" + searchText;
        String json = Utils.getPageJson(url);

        List<Restaurant> restaurantsList = new ArrayList<>();
        try {
            JSONArray restaurants = new JSONArray(json);
            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject restaurantJSONObject = (JSONObject) restaurants.get(i);
                Restaurant restaurant = new Restaurant();

                restaurant.setTitle(restaurantJSONObject.getString("title"));
                restaurant.setIntro(restaurantJSONObject.getString("intro"));
                restaurant.setIdRestaurant(restaurantJSONObject.getString("id"));
                restaurant.setAddress(restaurantJSONObject.getString("address"));
                restaurant.setCity(restaurantJSONObject.getString("city"));
                restaurant.setDescription(restaurantJSONObject.getString("description"));
                restaurant.setAverageCheck(restaurantJSONObject.getString("average_check"));
                restaurant.setPhone(restaurantJSONObject.getString("phone"));
                restaurant.setWebsite(restaurantJSONObject.getString("website"));
                restaurant.setAddressInstructions(restaurantJSONObject.getString("address_instructions"));

                if (restaurantJSONObject.isNull("photo_small_path")) {
                    restaurantJSONObject.put("photo_small_path", " ");
                }
                restaurant.setSmallPhoto(restaurantJSONObject.getString("photo_small_path"));

                if (restaurantJSONObject.isNull("photo_large_path")) {
                    restaurantJSONObject.put("photo_large_path", " ");
                }
                restaurant.setLargePhoto(restaurantJSONObject.getString("photo_large_path"));

                restaurantsList.add(restaurant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantsList;
    }

    private List<Restaurant> getRestaurantsList(String searchText, int pageRestaurants) {
        try {
            searchText = URLEncoder.encode(searchText, "UTF-8").replaceAll(" ", "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://osoboebludo.com/api/?notabs&json&content_id=16&keywords=" + searchText + "&page=" + pageRestaurants;
        String json = Utils.getPageJson(url);

        List<Restaurant> restaurantsList = new ArrayList<>();
        try {
            JSONArray restaurants = new JSONArray(json);
            for (int i = 0; i < restaurants.length(); i++) {
                JSONObject restaurantJSONObject = (JSONObject) restaurants.get(i);
                Restaurant restaurant = new Restaurant();

                restaurant.setTitle(restaurantJSONObject.getString("title"));
                restaurant.setIntro(restaurantJSONObject.getString("intro"));
                restaurant.setIdRestaurant(restaurantJSONObject.getString("id"));
                restaurant.setAddress(restaurantJSONObject.getString("address"));
                restaurant.setCity(restaurantJSONObject.getString("city"));
                restaurant.setDescription(restaurantJSONObject.getString("description"));
                restaurant.setAverageCheck(restaurantJSONObject.getString("average_check"));
                restaurant.setPhone(restaurantJSONObject.getString("phone"));
                restaurant.setWebsite(restaurantJSONObject.getString("website"));
                restaurant.setAddressInstructions(restaurantJSONObject.getString("address_instructions"));

                if (restaurantJSONObject.isNull("photo_small_path")) {
                    restaurantJSONObject.put("photo_small_path", " ");
                }
                restaurant.setSmallPhoto(restaurantJSONObject.getString("photo_small_path"));

                if (restaurantJSONObject.isNull("photo_large_path")) {
                    restaurantJSONObject.put("photo_large_path", " ");
                }
                restaurant.setLargePhoto(restaurantJSONObject.getString("photo_large_path"));

                restaurantsList.add(restaurant);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurantsList;
    }

    private List<Dish> getDishesList(String searchText) {
        try {
            searchText = URLEncoder.encode(searchText, "UTF-8").replaceAll(" ", "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://osoboebludo.com/api/?notabs&json&content_id=17&keywords=" + searchText;
        String json = Utils.getPageJson(url);

        List<Dish> dishesList = new ArrayList<>();
        try {
            JSONArray dishes = new JSONArray(json);
            for (int i = 0; i < dishes.length(); i++) {
                JSONObject dishJSONObject = (JSONObject) dishes.get(i);

                Dish dish = new Dish();
                dish.setTitle(dishJSONObject.getString("title"));
                dish.setIntro(dishJSONObject.getString("intro"));
                if (dishJSONObject.isNull("image")) {
                    dishJSONObject.put("image", " ");
                }
                dish.setLargePhoto(dishJSONObject.getString("image"));
                dish.setIdDish(dishJSONObject.getString("id"));
                dish.setRestaurantsAddress(dishJSONObject.getString("restaurants_address"));
                dish.setRestaurantsName(dishJSONObject.getString("restaurants"));
                dish.setDescription(dishJSONObject.getString("description"));
                dishesList.add(dish);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dishesList;
    }

    private List<Dish> getDishesList(String searchText, int pageDishes) {
        try {
            searchText = URLEncoder.encode(searchText, "UTF-8").replaceAll(" ", "+");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://osoboebludo.com/api/?notabs&json&content_id=17&keywords=" + searchText + "&page=" + pageDishes;
        String json = Utils.getPageJson(url);

        List<Dish> dishesList = new ArrayList<>();
        try {
            JSONArray dishes = new JSONArray(json);
            for (int i = 0; i < dishes.length(); i++) {
                JSONObject dishJSONObject = (JSONObject) dishes.get(i);

                Dish dish = new Dish();
                dish.setTitle(dishJSONObject.getString("title"));
                dish.setIntro(dishJSONObject.getString("intro"));
                if (dishJSONObject.isNull("image")) {
                    dishJSONObject.put("image", " ");
                }
                dish.setLargePhoto(dishJSONObject.getString("image"));
                dish.setIdDish(dishJSONObject.getString("id"));
                dish.setRestaurantsAddress(dishJSONObject.getString("restaurants_address"));
                dish.setRestaurantsName(dishJSONObject.getString("restaurants"));
                dish.setDescription(dishJSONObject.getString("description"));
                dishesList.add(dish);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dishesList;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(
                AdapterView<?> parent, View view, int position, long id
        ) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    private class AddDataToRestaurantsSimpleAdapter extends AsyncTask<Void, Void, ArrayList<Map<String, Object>>> {

        List<Restaurant> restaurantsList = getRestaurantsList(searchText, pageRestaurants);

        @Override
        protected ArrayList<Map<String, Object>> doInBackground(Void... params) {
            for (Restaurant each : restaurantsList) {
                RestaurantBitmap restaurantBitmap = new RestaurantBitmap();
                restaurantBitmap.setBitmapLargePhoto(each.getLargePhoto());
                Bitmap bitmap = restaurantBitmap.getBitmapLargePhoto();

                Map<String, Object> map = new HashMap<>();
                map.put(ATTRIBUTE_TITLE, each.getTitle());
                map.put(ATTRIBUTE_INTRO, each.getIntro());
                if (each.getLargePhoto() != " ") {
                    map.put(ATTRIBUTE_ICON, bitmap);
                }
                map.put(
                        ATTRIBUTE_RESTAURANT_ADDRESS,
                        getResources().getString(R.string.restaurantsAddressTemplate) + " " + each.getCity() + ", " + each.getAddress()
                );
                data.add(map);
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String, Object>> data) {
            super.onPostExecute(data);
            simpleAdapter.notifyDataSetChanged();
        }
    }
}
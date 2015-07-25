package com.osoboebludo.applicationosoboebludo;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ghost on 17.06.2015.
 */
public class RestaurantListAdapter implements ListAdapter, View.OnClickListener {

    private Context context;

    private List<Restaurant> restaurantsList;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantsList) {
        this.context = context;
        this.restaurantsList = restaurantsList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return restaurantsList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = restaurantsList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_search_dishes, null);
        }
        TextView tvTextTitle = (TextView) convertView.findViewById(R.id.tvTextTitle);
        tvTextTitle.setText(restaurant.getTitle());
        tvTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Restaurant restaurant = (Restaurant) view.g();
                Toast.makeText(view.getContext(), position + "", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvTextIntro = (TextView) convertView.findViewById(R.id.tvTextIntro);
        tvTextIntro.setText(restaurant.getIntro());

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onClick(View view) {
        Restaurant restaurant = (Restaurant) view.getTag();
        Toast.makeText(view.getContext(), restaurant.getTitle(), Toast.LENGTH_SHORT).show();
    }
}

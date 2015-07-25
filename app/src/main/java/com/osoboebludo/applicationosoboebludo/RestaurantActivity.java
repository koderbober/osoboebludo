package com.osoboebludo.applicationosoboebludo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RestaurantActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        Restaurant restaurant = (Restaurant) getIntent().getSerializableExtra("RESTAURANT");
        RestaurantBitmap restaurantBitmap = new RestaurantBitmap();
        restaurantBitmap.setBitmapLargePhoto(restaurant.getLargePhoto());

        LinearLayout restaurantLayout = (LinearLayout) findViewById(R.id.restaurantLayout);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(restaurant.getTitle());
        tvTitle.setTextColor(getResources().getColor(R.color.text));
        tvTitle.setTextSize(20);
        tvTitle.setPadding(0, 0, 0, 15);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        restaurantLayout.addView(tvTitle);

        ImageView restaurantImage = new ImageView(this);
        Bitmap bitmap = restaurantBitmap.getBitmapLargePhoto();
        restaurantImage.setImageBitmap(bitmap);
        restaurantLayout.addView(restaurantImage);

        TextView tvInformation = new TextView(this);
        tvInformation.setText(
                        getResources().getText(R.string.restaurantsAddressTemplate) + " " +
                        restaurant.getCity() + ", " +
                        restaurant.getAddress() + " " +
                        "(" + restaurant.getAddressInstructions() + ")" + "\n" +
                        getResources().getText(R.string.averageCheck) + " " +
                        restaurant.getAverageCheck() + "\n" +
                        getResources().getText(R.string.phone) + " " +
                        restaurant.getPhone() + "\n" +
                        getResources().getText(R.string.website) + " " +
                        restaurant.getWebsite()
        );
        tvInformation.setTextColor(getResources().getColor(R.color.text));
        Linkify.addLinks(tvInformation, Linkify.WEB_URLS);
        restaurantLayout.addView(tvInformation);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(restaurant.getDescription());
        tvDescription.setTextColor(getResources().getColor(R.color.text));
        restaurantLayout.addView(tvDescription);
    }
}

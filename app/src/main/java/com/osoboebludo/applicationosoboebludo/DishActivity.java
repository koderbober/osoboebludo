package com.osoboebludo.applicationosoboebludo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DishActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        Dish dish = (Dish) getIntent().getSerializableExtra("DISH");
        DishBitmap dishBitmap = new DishBitmap();
        dishBitmap.setBitmapLargePhoto(dish.getLargePhoto());

        LinearLayout dishLayout = (LinearLayout) findViewById(R.id.dishLayout);

        ImageView dishImage = new ImageView(this);
        Bitmap bitmap = dishBitmap.getBitmapLargePhoto();
        dishImage.setImageBitmap(bitmap);
        dishLayout.addView(dishImage);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(dish.getTitle());
        dishLayout.addView(tvTitle);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(dish.getDescription());
        dishLayout.addView(tvDescription);
    }
}

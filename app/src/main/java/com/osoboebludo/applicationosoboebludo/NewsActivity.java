package com.osoboebludo.applicationosoboebludo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;


public class NewsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        News news = (News) getIntent().getSerializableExtra("NEWS");
        NewsBitmap newsBitmap = new NewsBitmap();
        newsBitmap.setBitmapLargePhoto(news.getLargePhoto());

        LinearLayout newsLayout = (LinearLayout) findViewById(R.id.newsLayout);

        ImageView newsImage = new ImageView(this);
        Bitmap bitmap = newsBitmap.getBitmapLargePhoto();
        newsImage.setImageBitmap(bitmap);
        newsLayout.addView(newsImage);

        TextView tvDescription = new TextView(this);
        tvDescription.setText(news.getDescription());
        tvDescription.setScroller(new Scroller(this));
        tvDescription.setVerticalScrollBarEnabled(true);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setPadding(0, 20, 0, 20);
        tvDescription.setTextColor(Color.parseColor("#ff000000"));
        newsLayout.addView(tvDescription);
    }
}

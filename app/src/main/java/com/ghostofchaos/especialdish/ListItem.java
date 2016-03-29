package com.ghostofchaos.especialdish;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghostofchaos.especialdish.Objects.FeedModel;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

/**
 * Created by Ghost on 13.03.2016.
 */
public class ListItem extends AppCompatActivity {

    ImageView ivImg;
    TextView tvTextTitle, tvTextDescription, tvToolbarTitle;
    FeedModel feedModel;
    Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getApplicationContext());

        initializationUI();

        feedModel = (FeedModel) getIntent().getSerializableExtra("obj");
        Log.i("Model", feedModel.toString());

        setUI();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setUI() {
        Picasso.with(getApplicationContext())
                .load("http://osoboebludo.com/uploads/content/" + feedModel.getImage())
                .resize(MainActivity.width, 0)
                .into(ivImg);
        tvTextTitle.setText(Utils.specCharactersHtmlToString(feedModel.getTitle()));
//        tvTextDescription.setText(Utils.specCharactersHtmlToString(model.getDescription()));
        tvTextDescription.setText(Html.fromHtml(Html.fromHtml(feedModel.getDescription().replaceAll(Pattern.quote("\n"), "<br>")).toString().replaceAll(Pattern.quote("\n"), "<br>")));
        tvToolbarTitle.setText(Html.fromHtml("<b>" + getIntent().getStringExtra("title") + "</b>"));
    }

    private void initializationUI() {
        ivImg = (ImageView) findViewById(R.id.ivImg);
        tvTextTitle = (TextView) findViewById(R.id.tvTextTitle);
        tvTextTitle.setTypeface(typeface);
        tvTextDescription = (TextView) findViewById(R.id.tvTextDescription);
        tvTextDescription.setTypeface(typeface);
        tvToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        tvToolbarTitle.setTypeface(typeface);
    }
}

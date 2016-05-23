package com.ghostofchaos.especialdish;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.ghostofchaos.especialdish.Fragments.FragmentAbout;
import com.ghostofchaos.especialdish.Fragments.FragmentBlogs;
import com.ghostofchaos.especialdish.Fragments.FragmentCalendar;
import com.ghostofchaos.especialdish.Fragments.FragmentNews;
import com.ghostofchaos.especialdish.Fragments.FragmentProjects;
import com.ghostofchaos.especialdish.Fragments.FragmentReviews;
import com.ghostofchaos.especialdish.Fragments.FragmentSearch;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    Typeface typeface;
    TextView tvToolbar;
    FragmentNews fragmentNews;
    FragmentProjects fragmentProjects;
    FragmentBlogs fragmentBlogs;
    FragmentReviews fragmentReviews;
    FragmentCalendar fragmentCalendar;
    FragmentAbout fragmentAbout;
    FragmentSearch fragmentSearch;
    FragmentTransaction fTrans;
    public static int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentNews = new FragmentNews();
        fragmentProjects = new FragmentProjects();
        fragmentReviews = new FragmentReviews();
        fragmentBlogs = new FragmentBlogs();
        fragmentCalendar = new FragmentCalendar();
        fragmentAbout = new FragmentAbout();
        fragmentSearch = new FragmentSearch();

        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getApplicationContext());

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.container, fragmentSearch).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tvToolbar = (TextView) findViewById(R.id.toolbar_title);
        tvToolbar.setTypeface(typeface);
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.news) + "</b>"));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        Log.d("width", width + "");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        drawer.setDrawerListener(drawerToggle); // Drawer Listener set to the Drawer toggle
        drawerToggle.syncState();               // Finally we set the drawer toggle sync State

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withHeader(R.layout.drawer_header)
                .withHasStableIds(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.search).withDivider(false).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.search_restaurants).withIdentifier(1).withTypeface(typeface),
                        new SectionDrawerItem().withName(R.string.feed).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.news).withIdentifier(2).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.projects).withIdentifier(3).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.reviews).withIdentifier(4).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.blogs).withIdentifier(5).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.calendar).withIdentifier(6).withTypeface(typeface),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.about).withIdentifier(7).withTypeface(typeface),
                        new PrimaryDrawerItem().withName(R.string.help).withIdentifier(8).withTypeface(typeface)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
                        fTrans = getSupportFragmentManager().beginTransaction();

                        switch (position) {
                            case 1:
                                fTrans.replace(R.id.container, fragmentSearch, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 2:
                                fTrans.replace(R.id.container, fragmentNews, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 3:
                                fTrans.replace(R.id.container, fragmentProjects, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 4:
                                fTrans.replace(R.id.container, fragmentReviews, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 5:
                                fTrans.replace(R.id.container, fragmentBlogs, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 6:
                                fTrans.replace(R.id.container, fragmentCalendar, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            case 7:
                                fTrans.replace(R.id.container, fragmentAbout, "tag");
                                fTrans.addToBackStack("tag");
                                break;
                            default:
                                break;
                        }
                        fTrans.commit();
                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        FullScreenCall();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreenCall();
    }

    public void FullScreenCall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}

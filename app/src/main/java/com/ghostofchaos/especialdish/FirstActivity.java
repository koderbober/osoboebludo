package com.ghostofchaos.especialdish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ghostofchaos.especialdish.Fragments.FragmentMap;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Ghost on 26.05.2016.
 */
public class FirstActivity extends AppCompatActivity {

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView();

        sPref = getSharedPreferences("sPref", MODE_PRIVATE);
        boolean firstStart = sPref.getBoolean("firstStart", true);

        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext()).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        //if (firstStart) {

            DownloadObjectsManager.getInstance();
            DownloadObjectsManager.setHost(Adresses.GET_RESTAURANTS + Adresses.PAGE);
            DownloadObjectsManager.setPerPage(50);
            DownloadObjectsManager.setPage(0);
            DownloadObjectsManager.setModelArrayList(new ArrayList<Object>());
            DownloadObjectsManager.downloadObjects(getApplicationContext(), FragmentMap.map, true);

        //finish();

        /*} else {

            Intent intent = new Intent(FirstActivity.this, MainActivity.class);
            startActivity(intent);

        }*/

    }
}
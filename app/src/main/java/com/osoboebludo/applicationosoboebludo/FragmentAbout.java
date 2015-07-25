package com.osoboebludo.applicationosoboebludo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;

public class FragmentAbout extends Fragment {

    public String aboutScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        TextView text = (TextView)rootView.findViewById(R.id.txtLabel);
        aboutScreen = getResources().getString(R.string.about_screen);
        text.setText(aboutScreen);
        text.setMovementMethod(new ScrollingMovementMethod());

        return rootView;
    }
}
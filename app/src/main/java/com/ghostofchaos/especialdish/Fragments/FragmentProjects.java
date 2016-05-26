package com.ghostofchaos.especialdish.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostofchaos.especialdish.Adresses;
import com.ghostofchaos.especialdish.R;

/**
 * Created by Ghost on 07.02.2016.
 */
public class FragmentProjects extends FragmentMain {

    @Override
    public void setHost() {
        super.setHost();
        host = Adresses.GET_PROJECTS + Adresses.PAGE;
    }

    @Override
    public void setToolbar() {
        super.setToolbar();
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.projects) + "</b>"));
        toolbarTitle = getResources().getString(R.string.projects);
    }
}

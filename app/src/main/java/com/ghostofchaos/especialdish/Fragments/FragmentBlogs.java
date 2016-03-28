package com.ghostofchaos.especialdish.Fragments;

import android.text.Html;

import com.ghostofchaos.especialdish.R;

/**
 * Created by Ghost on 11.03.2016.
 */
public class FragmentBlogs extends FragmentMain {

    @Override
    public void setHost() {
        super.setHost();
        host = "http://osoboebludo.com/api/?notabs&json&content_id=14&page=";
    }

    @Override
    public void setToolbar() {
        super.setToolbar();
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.blogs) + "</b>"));
        toolbarTitle = getResources().getString(R.string.blogs);
    }
}

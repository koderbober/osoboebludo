package com.ghostofchaos.especialdish.Fragments;

import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghostofchaos.especialdish.FontCache;
import com.ghostofchaos.especialdish.R;

/**
 * Created by Ghost on 11.03.2016.
 */
public class FragmentAbout extends Fragment {

    TextView tvAbout;
    Typeface typeface;
    TextView tvToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_about, null);
        String fontName = "fonts/Proxima Nova Light.otf";
        typeface = FontCache.getTypeface(fontName, getActivity());
        tvAbout = (TextView) root.findViewById(R.id.tvAbout);
        tvAbout.setTypeface(typeface);
        tvToolbar = (TextView) getActivity().findViewById(R.id.toolbar_title);
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.about) + "</b>"));
        return root;
    }
}

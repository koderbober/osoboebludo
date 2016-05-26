package com.ghostofchaos.especialdish.Fragments;

import android.text.Html;

import com.ghostofchaos.especialdish.Adresses;
import com.ghostofchaos.especialdish.Objects.FeedModel;
import com.ghostofchaos.especialdish.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ghost on 11.03.2016.
 */
public class FragmentCalendar extends FragmentMain {

    @Override
    public void setHost() {
        super.setHost();
        host = Adresses.GET_CALENDAR + Adresses.PAGE;
    }

    @Override
    public void setToolbar() {
        super.setToolbar();
        tvToolbar.setText(Html.fromHtml("<b>" + getResources().getString(R.string.calendar) + "</b>"));
        toolbarTitle = getResources().getString(R.string.calendar);
    }

    @Override
    public void setList(String s) {
        super.setList(s);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        ArrayList<FeedModel> tempList = new ArrayList<>();

        for (FeedModel each : list) {
            try {
                date = format.parse(each.getDate_expired());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date.before(new Date())) {
                tempList.add(each);
            }
        }
        for (FeedModel each : tempList) {
            list.remove(each);
        }
    }

    @Override
    public void setPage() {
        super.setPage();
        page = 1;
    }
}

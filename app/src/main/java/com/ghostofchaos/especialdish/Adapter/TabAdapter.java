package com.ghostofchaos.especialdish.Adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ghostofchaos.especialdish.Fragments.FragmentNews;
import com.ghostofchaos.especialdish.Fragments.FragmentRestaurants;

import java.util.List;

/**
 * Created by Ghost on 27.03.2016.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES = { "Списком", "На карте" };
    FragmentRestaurants fragmentRestaurants;
    FragmentNews fragmentNews;

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (fragmentRestaurants == null) {
                    fragmentRestaurants = new FragmentRestaurants();
                }
                return fragmentRestaurants;
            case 1:
                if (fragmentNews == null) {
                    fragmentNews = new FragmentNews();
                }
                return fragmentNews;
        }
            return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}

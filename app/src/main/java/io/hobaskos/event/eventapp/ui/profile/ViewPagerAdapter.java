package io.hobaskos.event.eventapp.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Magnus on 15.03.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public final static String TAG = ViewPagerAdapter.class.getName();



    private List<Fragment> fragments;
    private List<String> titles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }


}

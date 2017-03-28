package io.hobaskos.event.eventapp.ui.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.profile.events.archived.ArchivedEventsFragment;
import io.hobaskos.event.eventapp.ui.profile.events.attending.AttendingEventsFragment;
import io.hobaskos.event.eventapp.ui.profile.events.mine.MyEventsFragment;


/**
 * Created by Magnus on 15.03.2017.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {
    public final static String TAG = ProfilePagerAdapter.class.getName();



    private AttendingEventsFragment attendingEventsFragment;
    private MyEventsFragment myEventsFragment;
    private ArchivedEventsFragment archivedEventsFragment;

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
        attendingEventsFragment = new AttendingEventsFragment();
        myEventsFragment = new MyEventsFragment();
        archivedEventsFragment = new ArchivedEventsFragment();
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.i(TAG, "getItem attending");
                return attendingEventsFragment;
            case 1:
                Log.i(TAG, "getItem myEvents");
                return myEventsFragment;
            case 2:
                Log.i(TAG, "getItem archived");
                return archivedEventsFragment;
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return App.getInst().getString(R.string.attending);
            case 1:
                return App.getInst().getString(R.string.mine);
            default:
                return App.getInst().getString(R.string.archived);
        }
    }

}

package io.hobaskos.event.eventapp.ui.profile;

import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.event.details.EventPagerAdapter;

/**
 * Created by Magnus on 15.03.2017.
 */

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {
    public final static String TAG = EventPagerAdapter.class.getName();

    private Context context;
    private Event event;
    private ArrayList<User> tmpUsers = new ArrayList<>();

    public ProfileFragmentPagerAdapter(Event event, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyEventsFragment.newInstance(event);
            case 1:
                return AttendingEventsFragment.newInstance(event);
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
                return context.getString(R.string.mine);
            default:
                return context.getString(R.string.attending);
        }
    }

}

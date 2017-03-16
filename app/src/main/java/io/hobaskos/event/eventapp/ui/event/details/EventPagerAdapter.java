package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesFragment;

/**
 * Created by alex on 3/10/17.
 */

public class EventPagerAdapter extends FragmentPagerAdapter {

    public final static String TAG = EventPagerAdapter.class.getName();

    private EventInfoFragment eventInfoFragment;
    private LocationsFragment locationsFragment;
    private AttendeesFragment attendeesFragment;

    private Context context;
    private Event event;

    public EventPagerAdapter(Event event, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;

        eventInfoFragment = EventInfoFragment.newInstance(event);
        locationsFragment = LocationsFragment.newInstance(event);
        attendeesFragment = AttendeesFragment.newInstance(event.getId());
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return eventInfoFragment;
            case 1:
                return locationsFragment;
            case 2:
                return attendeesFragment;
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.info);
            case 1:
                return context.getString(R.string.locations);
            default:
                return context.getString(R.string.attending);
        }
    }
}

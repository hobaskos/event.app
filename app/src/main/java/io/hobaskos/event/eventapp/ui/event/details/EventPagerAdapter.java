package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesFragment;

/**
 * Created by alex on 3/10/17.
 */

public class EventPagerAdapter extends FragmentStatePagerAdapter {

    public final static String TAG = EventPagerAdapter.class.getName();

    private Context context;
    private Event event;

    public EventPagerAdapter(Event event, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventInfoFragment.newInstance(event);
            case 1:
                return LocationsFragment.newInstance(event);
            case 2:
                return AttendeesFragment.newInstance(event.getId());
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

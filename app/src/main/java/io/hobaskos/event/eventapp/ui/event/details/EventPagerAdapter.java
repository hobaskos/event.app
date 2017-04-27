package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesFragment;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.CompetitionFragment;
import io.hobaskos.event.eventapp.ui.event.details.info.EventInfoFragment;
import io.hobaskos.event.eventapp.ui.event.details.location.LocationsFragment;

/**
 * Created by alex on 3/10/17.
 */

public class EventPagerAdapter extends FragmentPagerAdapter {

    public final static String TAG = EventPagerAdapter.class.getName();

    private EventInfoFragment eventInfoFragment;
    private LocationsFragment locationsFragment;
    private AttendeesFragment attendeesFragment;
    private CompetitionFragment competitionFragment;

    public static final int EVENT_INFO_FRAGMENT = 0;
    public static final int LOCATIONS_FRAGMENT = 1;
    public static final int ATTENDEES_FRAGMENT = 2;
    public static final int COMPETITIONS_FRAGMENT = 3;

    private Context context;
    private Event event;
    private boolean isOwner;
    private boolean isLoggedIn;

    public EventPagerAdapter(Event event, boolean isOwner, boolean isLoggedIn, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;
        this.isOwner = isOwner;
        this.isLoggedIn = isLoggedIn;

        Log.d(TAG, "EventPagerAdapter: " + event.getId() + ", myAttendance: " + event.getMyAttendance());

        eventInfoFragment = EventInfoFragment.newInstance(event);
        locationsFragment = LocationsFragment.newInstance(event.getId(), isOwner);
        attendeesFragment = AttendeesFragment.newInstance(event.getId(), event.getMyAttendance() != null);
        if (isLoggedIn) {
            competitionFragment = CompetitionFragment.newInstance(event.getDefaultPollId(), event.isAttending(), false);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return eventInfoFragment;
            case 1: return locationsFragment;
            case 2: return attendeesFragment;
            case 3: if(isLoggedIn) return competitionFragment;
            default: return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return isLoggedIn ? 4 : 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }


}

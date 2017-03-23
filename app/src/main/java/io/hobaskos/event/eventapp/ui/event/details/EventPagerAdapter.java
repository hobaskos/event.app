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
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesFragment;
import io.hobaskos.event.eventapp.ui.event.details.competition.CompetitionFragment;
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

    private Context context;
    private Event event;
    private boolean isOwner;
    private boolean isLoggedIn;
    private ArrayList<CompetitionImage> competitionImages;

    public EventPagerAdapter(Event event, ArrayList<CompetitionImage> competitionImages, boolean isOwner, boolean isLoggedIn, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;
        this.isOwner = isOwner;
        this.isLoggedIn = isLoggedIn;
        this.competitionImages = competitionImages;

        Log.d(TAG, "EventPagerAdapter: " + event.getId() + ", myAttendance: " + event.getMyAttendance());

        eventInfoFragment = EventInfoFragment.newInstance(event);
        locationsFragment = LocationsFragment.newInstance(event, isOwner);
        attendeesFragment = AttendeesFragment.newInstance(event.getId(), event.getMyAttendance() != null);
        competitionFragment = CompetitionFragment.newInstance(competitionImages, isLoggedIn);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.i(TAG, "getItem eventInfoFragment");
                return eventInfoFragment;
            case 1:
                Log.i(TAG, "getItem locationsFragment");
                return locationsFragment;
            case 2:
                Log.i(TAG, "getItem attendeesFragment");
                return attendeesFragment;
            case 3:
                Log.i(TAG, "getItem competitionFragment");
                return competitionFragment;
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.info);
            case 1:
                return context.getString(R.string.locations);
            case 2:
                return context.getString(R.string.attending);
            default:
                return context.getString(R.string.competitions);
        }
    }


}

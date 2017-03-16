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

/**
 * Created by alex on 3/10/17.
 */

public class EventPagerAdapter extends FragmentPagerAdapter {

    public final static String TAG = EventPagerAdapter.class.getName();

    private EventInfoFragment eventInfoFragment;
    private LocationsFragment locationsFragment;
    private UsersFragment usersFragment;

    private Context context;
    private Event event;
    private ArrayList<User> tmpUsers = new ArrayList<>();

    public EventPagerAdapter(Event event, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;

        tmpUsers.add(new User("Frank", "Olsen"));
        tmpUsers.add(new User("Kenneth", "Nilsen"));
        tmpUsers.add(new User("Lennart", "Paulsen"));

        eventInfoFragment = EventInfoFragment.newInstance(event);
        locationsFragment = LocationsFragment.newInstance(event);
        usersFragment = UsersFragment.newInstance(tmpUsers);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return eventInfoFragment;
            case 1:
                return locationsFragment;
            case 2:
                return usersFragment;
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

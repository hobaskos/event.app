package io.hobaskos.event.eventapp.ui.profile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.details.EventPagerAdapter;

/**
 * Created by Magnus on 15.03.2017.
 */

public class ProfileFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public final static String TAG = EventPagerAdapter.class.getName();

    private Context context;
    private Event event;

    public ProfileFragmentPagerAdapter(Event event, Context context, FragmentManager fm) {
        super(fm);
        this.event = event;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return AttendingEventsFragment.newInstance(position);
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

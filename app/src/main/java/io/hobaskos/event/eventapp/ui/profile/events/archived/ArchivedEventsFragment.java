package io.hobaskos.event.eventapp.ui.profile.events.archived;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.hobaskos.event.eventapp.R;

/**
 * Created by test on 3/16/2017.
 */

public class ArchivedEventsFragment extends Fragment {

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        return inflater.inflate(R.layout.fragment_test_tab, container, false);
    }
}

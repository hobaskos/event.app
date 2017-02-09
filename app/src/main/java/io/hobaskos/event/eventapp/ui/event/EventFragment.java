package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.hobaskos.event.eventapp.R;

/**
 * Created by Magnus on 09.02.2017.
 */

public class EventFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment, container, false);
        return view;
    }
}

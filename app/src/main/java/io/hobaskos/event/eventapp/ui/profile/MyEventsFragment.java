package io.hobaskos.event.eventapp.ui.profile;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by Magnus on 13.03.2017.
 */

public class MyEventsFragment extends Fragment{

    private static final String ARG_EVENT = "event";
    private Event event;

    public MyEventsFragment() {}

    public static MyEventsFragment newInstance(Event event) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            event = getArguments().getParcelable(ARG_EVENT);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
/*
        eventImage = (ImageView) view.findViewById(R.id.image);
        eventTime = (TextView) view.findViewById(R.id.date);
        eventDescription = (TextView)  view.findViewById(R.id.description);

        Picasso.with(getContext()) // just some random image for now
                .load("https://mave.me/img/projects/full_placeholder.png")
                .into(eventImage);

        eventTime.setText(DateUtils.formatDateTime(getContext(),
                event.getFromDate().toDate().getTime(), DateUtils.FORMAT_SHOW_DATE));
        eventDescription.setText(event.getDescription());
*/
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}//End of class

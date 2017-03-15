package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;

public class EventInfoFragment extends Fragment {

    private static final String ARG_EVENT = "event";

    private ImageView eventImage;
    private TextView eventTime;
    private TextView eventDescription;
    private Event event;

    public EventInfoFragment() {}

    public static EventInfoFragment newInstance(Event event) {
        EventInfoFragment fragment = new EventInfoFragment();
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

        eventImage = (ImageView) view.findViewById(R.id.image);
        eventTime = (TextView) view.findViewById(R.id.date);
        eventDescription = (TextView)  view.findViewById(R.id.description);

        Picasso.with(getContext()) // just some random image for now
                .load("https://mave.me/img/projects/full_placeholder.png")
                .into(eventImage);

        if(event.getFromDate() != null) {
            eventTime.setText(DateUtils.formatDateTime(getContext(),
                    event.getFromDate().toDate().getTime(), DateUtils.FORMAT_SHOW_DATE));
        }

        eventDescription.setText(event.getDescription());

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
}

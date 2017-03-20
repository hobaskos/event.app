package io.hobaskos.event.eventapp.ui.event.details.info;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;

public class EventInfoFragment extends Fragment {

    private static final String ARG_EVENT = "event";
    private static final String EVENT_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";

    @BindView(R.id.image)
    protected ImageView eventImage;
    @BindView(R.id.date)
    protected TextView eventTime;
    @BindView(R.id.category)
    protected TextView category;
    @BindView(R.id.description)
    protected TextView eventDescription;
    @BindView(R.id.attendance_count)
    protected TextView attendanceCount;
    @BindView(R.id.my_attendance)
    protected TextView myAttendance;

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

        ButterKnife.bind(this, view);

        Picasso.with(getContext())
                .load(event.getImageUrl() != null ? event.getAbsoluteImageUrl() : EVENT_IMAGE_URL_PLACEHOLDER)
                .into(eventImage);

        if(event.getFromDate() != null) {
            eventTime.setText(DateUtils.formatDateTime(getContext(),
                    event.getFromDate().toDate().getTime(), DateUtils.FORMAT_SHOW_DATE));
        }

        category.setText(event.getCategory().getTitle());
        eventDescription.setText(event.getDescription());
        attendanceCount.setText(String.valueOf(event.getAttendanceCount()));

        if (event.getMyAttendance() != null) {
            myAttendance.setText(R.string.attending_event);
        }

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

    public void refresh(Event event) {
        Log.i("EventInfoFragment", "Refreshing EventInfoFragment");
        this.event = event;

        Picasso.with(getContext())
                .load(event.getImageUrl() != null ? event.getAbsoluteImageUrl() : EVENT_IMAGE_URL_PLACEHOLDER)
                .into(eventImage);

        if(event.getFromDate() != null) {
            eventTime.setText(DateUtils.formatDateTime(getContext(),
                    event.getFromDate().toDate().getTime(), DateUtils.FORMAT_SHOW_DATE));
        }

        eventDescription.setText(event.getDescription());

        attendanceCount.setText(String.valueOf(event.getAttendanceCount()));

        if(event.getMyAttendance() != null) {
            myAttendance.setText(R.string.attending_event);
        }

    }
}

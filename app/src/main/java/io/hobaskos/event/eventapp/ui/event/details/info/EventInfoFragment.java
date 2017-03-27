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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.util.UrlUtil;

public class EventInfoFragment extends Fragment {

    private static final String ARG_EVENT = "event";
    private static final String EVENT_IMAGE_URL_PLACEHOLDER = "https://mave.me/img/projects/full_placeholder.png";

    @BindView(R.id.image)
    protected ImageView eventImage;
    @BindView(R.id.date)
    protected TextView eventTime;
    @BindView(R.id.description)
    protected TextView eventDescription;
    @BindView(R.id.attendance_count)
    protected TextView attendanceCount;
    @BindView(R.id.my_attendance)
    protected TextView myAttendance;
    @BindView(R.id.private_event)
    protected LinearLayout privateEventWrapper;
    @BindView(R.id.private_event_invite_code)
    protected TextView inviteCode;
    @BindView(R.id.organizer)
    protected TextView organizer;
    @BindView(R.id.category)
    protected TextView category;

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
                .load(UrlUtil.getImageUrl(event.getImageUrl()))
                .into(eventImage);

        if(event.getFromDate() != null) {
            eventTime.setText(DateUtils.formatDateTime(getContext(),
                    event.getFromDate().toDate().getTime(), DateUtils.FORMAT_SHOW_DATE));
        }

        eventDescription.setText(event.getDescription());
        attendanceCount.setText(String.valueOf(event.getAttendanceCount()));
        organizer.setText("REPLACE ME");
        category.setText(event.getCategory().getTitle());

        if (event.isPrivateEvent()) {
            privateEventWrapper.setVisibility(View.VISIBLE);
            inviteCode.setText(event.getInvitationCode());
        }

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

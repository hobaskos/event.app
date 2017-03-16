package io.hobaskos.event.eventapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.list.EventsAdapter;

/**
 * Created by Magnus on 13.03.2017.
 */

public class AttendingEventsFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String ARG_EVENT_LIST = "event-list";
    private static final String ARG_EVENT_ID = "eventId";

    private ArrayList<Event> eventAttending = new ArrayList<>();



    public static AttendingEventsFragment newInstance(ArrayList<Event> event) {
        AttendingEventsFragment fragment = new AttendingEventsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_EVENT_LIST, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventAttending = getArguments().getParcelableArrayList(ARG_EVENT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new EventsAdapter(eventAttending,context, event -> {
            Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra(EventActivity.EVENT_ID, event.getId());
            intent.putExtra(EventActivity.EVENT_THEME, event.getCategory().getTheme());
            startActivity(intent);
        }));

        return view;
    }



}//End of class
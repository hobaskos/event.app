package io.hobaskos.event.eventapp.ui.profile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.api.AccountService;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.details.LocationRecyclerViewAdapter;
import io.hobaskos.event.eventapp.ui.location.add.LocationActivity;

/**
 * Created by Magnus on 13.03.2017.
 */

public class AttendingEventsFragment extends Fragment {
    private static final String ARG_EVENT_LIST = "event-list";
    private static final String ARG_EVENT_ID = "eventId";

    private ArrayList<Event> eventAttending = new ArrayList<>();
    private AttendingEventsFragment.OnListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;
    private Long eventId;

    public AttendingEventsFragment() {}

    @SuppressWarnings("unused")
    public static AttendingEventsFragment newInstance(Event event) {
        Fragment fragment = new AttendingEventsFragment();
        Bundle args = new Bundle();


        args.putParcelableArrayList(ARG_EVENT_LIST, (ArrayList<Event>));
        args.putParcelableArray(ARG_EVENT_LIST,(ArrayList<Event>) );
        args.putLong(ARG_EVENT_ID, event.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            eventAttending = getArguments().getParcelableArrayList(ARG_EVENT_LIST);
            eventId = getArguments().getLong(ARG_EVENT_ID);
        }
    }

}//End of class







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            locations = getArguments().getParcelableArrayList(ARG_LOCATIONS_LIST);
            eventId = getArguments().getLong(ARG_EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);


        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new LocationRecyclerViewAdapter(locations, listener));

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        addLocation = (FloatingActionButton) view.findViewById(R.id.fragment_location_list_fab);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof io.hobaskos.event.eventapp.ui.event.details.LocationsFragment.OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event item);
    }
}
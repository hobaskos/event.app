package io.hobaskos.event.eventapp.ui.event.details.location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.ui.location.add.LocationActivity;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocationsFragment extends Fragment {

    private static final String ARG_LOCATIONS_LIST = "locations-list";
    private static final String ARG_EVENT_ID = "eventId";

    private ArrayList<Location> locations = new ArrayList<>();
    private OnListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;
    private FloatingActionButton addLocation;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private Long eventId;

    public LocationsFragment() {}

    @SuppressWarnings("unused")
    public static LocationsFragment newInstance(Event event) {
        LocationsFragment fragment = new LocationsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LOCATIONS_LIST, (ArrayList<Location>) event.getLocations());
        args.putLong(ARG_EVENT_ID, event.getId());
        fragment.setArguments(args);
        return fragment;
    }

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
        locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(locations, listener, context);
        recyclerView.setAdapter(locationRecyclerViewAdapter);

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        addLocation = (FloatingActionButton) view.findViewById(R.id.fragment_location_list_fab);
        addLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LocationActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
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
        void onListFragmentEditInteraction(Location item);
        void onListFragmentDeleteInteraction(Location item);
    }

    public void refresh(ArrayList<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        locationRecyclerViewAdapter.notifyDataSetChanged();
    }


}

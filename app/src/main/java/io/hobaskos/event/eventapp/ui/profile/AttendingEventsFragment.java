package io.hobaskos.event.eventapp.ui.profile;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.details.LocationRecyclerViewAdapter;

/**
 * Created by Magnus on 13.03.2017.
 */

public class AttendingEventsFragment extends Fragment {
    private static final String ARG_ATTENDING_EVENTS = "attending_events";

    private ArrayList<Event> events = new ArrayList<>();
    private OnListFragmentInteractionListener listener;
    private DividerItemDecoration dividerItemDecoration;

    public AttendingEventsFragment() {

    }

    @SuppressWarnings("unused")
    public static AttendingEventsFragment newInstance(ArrayList<Event> events) {
        AttendingEventsFragment fragment = new AttendingEventsFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARG_ATTENDING_EVENTS, events);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList(ARG_ATTENDING_EVENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_att_event_list, container, false);
        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new AttEventRecyclerViewAdapter(events, listener));

        dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        listener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Event item);
    }

}//End of class
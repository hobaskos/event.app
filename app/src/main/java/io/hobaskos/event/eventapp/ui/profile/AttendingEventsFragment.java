package io.hobaskos.event.eventapp.ui.profile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsAdapter;

/**
 * Created by Magnus on 13.03.2017.
 */

public class AttendingEventsFragment extends MvpFragment<AttendingEventsView, AttendingEventsPresenter> implements AttendingEventsView{

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private ArrayList<Event> eventAttending = new ArrayList<>();

    @Inject
    public AttendingEventsPresenter presenter;

    public static AttendingEventsFragment newInstance(int page) {
        AttendingEventsFragment fragment = new AttendingEventsFragment();

        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, page);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }

        //presenter.getEventUserAttending();


    }

    @NonNull
    @Override
    public AttendingEventsPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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


    @Override
    public void setEventAttending(List<Event> eventsAttending) {
        eventAttending.addAll(eventsAttending);
    }

}//End of class
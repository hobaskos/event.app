package io.hobaskos.event.eventapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.BaseMvpFragment;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;
import io.hobaskos.event.eventapp.ui.event.EventActivity;
import rx.Observer;


public class EventsFragment extends BaseMvpFragment<EventsPresenter> implements Observer<List<Event>>  {

    public final static String TAG = EventsActivity.class.getName();

    // Views
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    // Model
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Inject
    public EventsPresenter eventsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout:
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        // Find views:
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        // Configure toolbar:
        configureToolbar();


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsAdapter = new EventsAdapter(eventsList,
                event -> {
                    Intent intent = new Intent(getActivity(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, event.getId());
                    startActivity(intent);
                },
                itemCount -> {
                    showLoading();
                    eventsPresenter.requestNext();
                });

        recyclerView.setAdapter(eventsAdapter);



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @NonNull
    @Override
    protected String tag() {
        return TAG;
    }

    @NonNull
    @Override
    protected PresenterFactory<EventsPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> eventsPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventsPresenter presenter) {
        this.eventsPresenter = presenter;
        eventsPresenter.subscribe(this);
    }

    @Override
    public void onCompleted() {
        // not needed as of now.
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext(List<Event> events) {
        eventsList.addAll(events);
        eventsAdapter.notifyDataSetChanged();
        stopLoading();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void configureToolbar() {
        setHasOptionsMenu(true);
        toolbar.setTitle("Events");
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.action_search:
                    // TODO: Create search fragment/activity
                    return true;
                case R.id.action_filter:
                    // TODO: Create filter fragment/activity
                    return true;
                case R.id.action_create:
                    // TODO: Create "create event" fragment/activity
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

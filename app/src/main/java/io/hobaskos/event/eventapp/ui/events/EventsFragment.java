package io.hobaskos.event.eventapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    private RecyclerView list;
    private ProgressBar progressBar;

    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Inject
    public EventsPresenter eventsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        list = (RecyclerView) view.findViewById(R.id.list);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
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

        list.setAdapter(eventsAdapter);

        return view;
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
}

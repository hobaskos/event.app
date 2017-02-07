package io.hobaskos.event.eventapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.BaseMvpActivity;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;
import io.hobaskos.event.eventapp.ui.event.EventActivity;
import rx.Observer;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsActivity extends BaseMvpActivity<EventsPresenter> implements Observer<List<Event>> {

    public final static String TAG = EventsActivity.class.getName();

    private RecyclerView list;
    private ProgressBar progressBar;

    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter;

    @Inject public EventsPresenter eventsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderView();
        init();
    }

    @NonNull
    @Override
    protected String tag() {
        return TAG;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventsPresenter presenter) {
        Log.i(TAG, "onPresenterPrepared");
        this.eventsPresenter = presenter;
        eventsPresenter.subscribe(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<EventsPresenter> getPresenterFactory() {
        Log.i(TAG, "getPresenterFactory");
        App.getInst().getComponent().inject(this);
        return () -> eventsPresenter;
    }

    public  void renderView() {
        setContentView(R.layout.activity_events);
        list = (RecyclerView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public void init() {
        Log.i(TAG, "init");

        list.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventsAdapter(eventsList,
                event -> {
                    Intent intent = new Intent(this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, event.getId());
                    startActivity(intent);
                },
                itemCount -> {
                    showLoading();
                    eventsPresenter.requestNext();
                });

        list.setAdapter(eventsAdapter);
        showLoading();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCompleted() {
        // not needed as of now.
    }

    @Override
    public void onError(Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNext(List<Event> events) {
        Log.i(TAG, "setData size - " + events.size());

        eventsList.addAll(events);
        eventsAdapter.notifyDataSetChanged();
        stopLoading();
    }

    public List<Event> getEventsList() {
        return eventsList;
    }
}

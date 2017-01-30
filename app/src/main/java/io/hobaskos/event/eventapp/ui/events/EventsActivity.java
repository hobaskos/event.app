package io.hobaskos.event.eventapp.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.EventActivity;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsActivity extends AppCompatActivity implements EventsView {

    private RecyclerView list;
    private ProgressBar progressBar;

    private List<Event> eventsList;

    @Inject public EventsPresenter eventsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInst().getComponent().inject(this);

        renderView();
        init();

        eventsPresenter.attachView(this);
        eventsPresenter.getEvents();
    }

    public  void renderView(){
        setContentView(R.layout.activity_events);
        list = (RecyclerView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    public void init(){
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {

    }

    public void stopLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String appErrorMessage) {
        Toast.makeText(this, appErrorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(List<Event> events) {

        this.eventsList = events;
        stopLoading();

        EventsAdapter adapter = new EventsAdapter(getApplicationContext(), events,
                Item -> {
                    Intent intent = new Intent(this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_ID, Item.getId());
                    startActivity(intent);
                });

        list.setAdapter(adapter);
    }

    public List<Event> getEventsList() {
        return eventsList;
    }
}

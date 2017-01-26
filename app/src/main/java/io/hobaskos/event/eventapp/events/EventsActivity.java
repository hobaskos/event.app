package io.hobaskos.event.eventapp.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.models.Event;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsActivity extends AppCompatActivity implements EventsView {

    private RecyclerView list;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getDeps().inject(this);

        renderView();
        init();

        EventsPresenter presenter = new EventsPresenter(this);
        presenter.getEvents();
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
    public void showWait() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String appErrorMessage) {

    }

    @Override
    public void setEvents(List<Event> events) {

        EventsAdapter adapter = new EventsAdapter(getApplicationContext(), events,
                new EventsAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Event Item) {
                        Toast.makeText(getApplicationContext(), Item.getTitle(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        list.setAdapter(adapter);

    }
}

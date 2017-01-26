package io.hobaskos.event.eventapp.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.models.Event;

/**
 * Created by andre on 1/26/2017.
 */

public class EventActivity extends AppCompatActivity implements EventView {

    TextView textViewTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        renderView();
        init();

        EventPresenter presenter = new EventPresenter(this);
        presenter.getEvent(1);
    }

    public void renderView(){
        setContentView(R.layout.activity_event);

        textViewTitle = (TextView) findViewById(R.id.event_title);
    }

    public void init(){
        //list.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void showWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void setEvent(Event event) {
        textViewTitle.setText(event.getTitle());
    }
}

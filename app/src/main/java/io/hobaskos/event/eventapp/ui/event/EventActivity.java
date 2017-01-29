package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 1/26/2017.
 */

public class EventActivity extends AppCompatActivity implements EventView {

    public final static String EVENT_ID = "eventId";

    @Inject public EventPresenter eventPresenter;

    //@BindView(R.id.event_title)
    private TextView eventTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInst().getComponent().inject(this);
        //ButterKnife.bind(this);

        renderView();
        init();

        Long eventId = getIntent().getExtras().getLong(EVENT_ID);

        eventPresenter.attachView(this);
        eventPresenter.getEvent(eventId);
    }

    public void renderView() {
        setContentView(R.layout.activity_event);

        eventTitle = (TextView) findViewById(R.id.event_title);
    }

    public void init(){
        //list.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(Event data) {
        eventTitle.setText(data.getTitle());
    }
}

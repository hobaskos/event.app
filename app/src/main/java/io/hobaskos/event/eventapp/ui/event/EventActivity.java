package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.BaseMvpActivity;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;

/**
 * Created by andre on 1/26/2017.
 */

public class EventActivity extends BaseMvpActivity<EventPresenter, EventView> implements EventView<Event> {

    public final static String EVENT_ID = "eventId";

    //@BindView(R.id.event_title1) TextView eventTitle;

    TextView eventTitle;

    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_event);
        //ButterKnife.bind(this);
        eventTitle = (TextView) findViewById(R.id.event_title1);
        eventTitle.setText("TEST");

        //Long eventId = getIntent().getExtras().getLong(EVENT_ID);
        Long eventId = Long.valueOf(1004);

        //eventPresenter.onViewAttached(this);
        //eventPresenter.getEvent(eventId);
    }

    @NonNull
    @Override
    protected String tag() {
        return "activity";
    }

    @NonNull
    @Override
    protected PresenterFactory<EventPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> eventPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventPresenter presenter) {
        Log.i("event-activity", "Prev presenter");
        this.eventPresenter = presenter;
        eventPresenter.onViewAttached(this);
        Long eventId = Long.valueOf(1004);
        eventPresenter.getEvent(eventId);
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
        Log.i("event-activity", errorMessage);
        //eventTitle.setText("TEST");
    }



    @Override
    public void setData(Event data) {
        eventTitle.setText(data.getTitle() + EventPresenter.counter);
        //eventTitle.setText("TEST");
    }
}

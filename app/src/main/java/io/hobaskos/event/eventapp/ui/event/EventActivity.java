package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.BasePresenterActivity;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;

/**
 * Created by andre on 1/26/2017.
 */

public class EventActivity extends BasePresenterActivity<EventPresenter, EventView> implements EventView<Event> {

    public final static String EVENT_ID = "eventId";

    @BindView(R.id.event_title1) TextView eventTitle;


    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getInst().getComponent().inject(this);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        eventTitle.setText("TEST");

        //Long eventId = getIntent().getExtras().getLong(EVENT_ID);
        Long eventId = Long.valueOf(1003);

        eventPresenter.onViewAttached(this);
        eventPresenter.getEvent(eventId);
    }

    @NonNull
    @Override
    protected String tag() {
        return "activity";
    }

    @NonNull
    @Override
    protected PresenterFactory<EventPresenter> getPresenterFactory() {
        return () -> eventPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventPresenter presenter) {
        this.eventPresenter = presenter;
    }


    private void renderView() {
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
    }

    private void init(){
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
        Log.i("event-activity", errorMessage);
        //eventTitle.setText("TEST");
    }



    @Override
    public void setData(Event data) {
        //eventTitle.setText(data.getTitle());
        //eventTitle.setText("TEST");
    }
}

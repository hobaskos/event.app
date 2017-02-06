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
import rx.Observer;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseMvpActivity<EventPresenter> implements Observer<Event> {

    public final static String EVENT_ID = "eventId";
    public final static String TAG = EventActivity.class.getName();

    private Long eventId;

    //@BindView(R.id.event_title1) TextView eventTitle;

    private TextView eventTitle;

    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        //ButterKnife.bind(this);
        eventTitle = (TextView) findViewById(R.id.event_title1);
        eventTitle.setText("TEST");

        eventId = getIntent().getExtras().getLong(EVENT_ID);
    }

    @NonNull
    @Override
    protected String tag() {
        return TAG;
    }

    @NonNull
    @Override
    protected PresenterFactory<EventPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> eventPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull EventPresenter presenter) {
        Log.i(TAG, "onPresenterPrepared");
        this.eventPresenter = presenter;
        eventPresenter.getEvent(eventId);
        eventPresenter.subscribe(this);
    }

    @Override
    public void onNext(Event event) {
        eventTitle.setText(String.format("ID:%d, TITLE:%s", event.getId(), event.getTitle()));
    }

    @Override
    public void onError(Throwable e) {
        Log.i("event-activity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted() {
        // not needed as of now.
    }
}

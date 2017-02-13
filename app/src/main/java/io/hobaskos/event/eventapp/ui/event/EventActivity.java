package io.hobaskos.event.eventapp.ui.event;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


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

    //private TextView eventTitle;
    private TextView eventTitle;
    private TextView date;
    private ImageView eventImg;
    private TextView eventTime;
    private TextView eventPlace;
    private TextView eventDescription;
    private TextView eventInterested;
    private TextView eventAttending;
    private TextView eventFriends;


    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_fragment);
        //setContentView(R.layout.activity_event);

        //ButterKnife.bind(this);

        eventTitle = (TextView) findViewById(R.id.event_name);
        date = (TextView) findViewById(R.id.date_value);
        eventTime = (TextView) findViewById(R.id.time_text);
        eventPlace = (TextView) findViewById(R.id.place_text);
        eventDescription = (TextView) findViewById(R.id.description_text);
        eventImg = (ImageView) findViewById(R.id.eventImage);
        eventInterested = (TextView) findViewById(R.id.interested_value);
        eventAttending = (TextView) findViewById(R.id.attending_value);
        eventFriends = (TextView) findViewById(R.id.friends_value);


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
        // Event Title
        eventTitle.setText(String.format(event.getTitle()));

        // Event date
        date.setText(DateUtils.getRelativeTimeSpanString(event.getFromDate().toDate().getTime()));

        // Event Image
        Picasso.with(this).load(event.getImageUrl()).into(eventImg);

        // Event Time
        eventTime.setText(String.format(event.getFromDate().getHourOfDay()+"."+event.getFromDate().getMinuteOfHour()+ " - " + event.getToDate().getHourOfDay()+"."+event.getToDate().getMinuteOfHour()));

        // Event Place
        eventPlace.setText(String.format(event.getLocations().toString()));

        // Event interested
        eventInterested.setText(String.format("30"));
        // Event attending
        eventAttending.setText(String.format("20"));
        // Event friends
        eventFriends.setText(String.format("10"));

        // Event Description
        eventDescription.setText(String.format(event.getDescription()));




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

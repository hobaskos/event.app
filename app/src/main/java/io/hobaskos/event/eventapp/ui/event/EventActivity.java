package io.hobaskos.event.eventapp.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseLceViewStateActivity;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseLceViewStateActivity<RelativeLayout, Event, EventView,
        EventPresenter> implements EventView {

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
    private Button mapButton;

    private Event event;

    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_event);
        super.onCreate(savedInstanceState);

        eventTitle = (TextView) findViewById(R.id.event_name);
        date = (TextView) findViewById(R.id.date_value);
        eventTime = (TextView) findViewById(R.id.time_text);
        eventPlace = (TextView) findViewById(R.id.place_text);
        mapButton = (Button) findViewById(R.id.mapButton);
        eventDescription = (TextView) findViewById(R.id.description_text);
        eventImg = (ImageView) findViewById(R.id.eventImage);
        eventInterested = (TextView) findViewById(R.id.interested_value);
        eventAttending = (TextView) findViewById(R.id.attending_value);
        eventFriends = (TextView) findViewById(R.id.friends_value);
        eventId = getIntent().getExtras().getLong(EVENT_ID);

        //Event Handler
        mapButton.setOnClickListener((View v) -> {
            //Intent intent = new Intent(this, MapsActivity.class);
            //intent.putExtra()
            startActivity(new Intent(this, MapsActivity.class));
        });
    }

    @NonNull
    @Override
    public EventPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return eventPresenter;
    }

    @Override
    public EventViewState createViewState() {
        return new EventViewState();
    }

    @Override
    public EventViewState getViewState() {
        Log.i(TAG, "getViewState()");
        return (EventViewState) super.getViewState();
    }

    @Override public void onNewViewStateInstance() {
        Log.i(TAG, "onNewViewStateInstance()");
        loadData(false); // load data from presenter
    }

    @Override
    public Event getData() {
        return event;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        Log.i("event-activity", e.getMessage());
        //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        return e.getMessage();
    }

    @Override
    public void setData(Event event) {
        this.event = event;
        // Event Title
        eventTitle.setText(String.format(event.getTitle()));

        // Event date
        //date.setText(DateUtils.getRelativeTimeSpanString(event.getFromDate().toDate().getTime()));

        // Event Image
        Picasso.with(this).load(event.getImageUrl()).into(eventImg);

        // Event Time
        //eventTime.setText(String.format(event.getFromDate().getHourOfDay()+"."+event.getFromDate().getMinuteOfHour()+ " - " + event.getToDate().getHourOfDay()+"."+event.getToDate().getMinuteOfHour()));

        // Event Place
        if (!event.getLocations().isEmpty()) {
            eventPlace.setText(event.getLocations().get(0).getName());
        }

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
    public void loadData(boolean pullToRefresh) {
        presenter.getEvent(eventId);
    }
}

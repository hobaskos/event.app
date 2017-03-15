package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseLceViewStateActivity;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseLceViewStateActivity<RelativeLayout, Event, EventView,
        EventPresenter> implements
        EventView,
        LocationsFragment.OnListFragmentInteractionListener,
        UsersFragment.OnUserListFragmentInteractionListener {

    public final static String EVENT_ID = "eventId";
    public final static String EVENT_THEME = "eventTheme";
    public final static String TAG = EventActivity.class.getName();

    private Long eventId;

    protected ViewPager viewPager;
    protected TabLayout tabLayout;

    private Event event;

    @Inject public EventPresenter eventPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This must be before setContentView!
        EventCategoryTheme theme = (EventCategoryTheme) getIntent().getExtras().getSerializable(EVENT_THEME);
        if (theme != null) { setEventTheme(theme); }

        setContentView(R.layout.activity_event);
        setTitle(R.string.loading);
        getSupportActionBar().setElevation(0);


        eventId = getIntent().getExtras().getLong(EVENT_ID);
        // eventId = 1231231L; // TODO: Can be removed. Used to test invalid event id
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    private void setEventTheme(EventCategoryTheme theme) {
        switch (theme) {
            case RED:
                setTheme(R.style.AppTheme_Red);
                break;
            case ORANGE:
                setTheme(R.style.AppTheme_Orange);
                break;
            case YELLOW:
                setTheme(R.style.AppTheme_Yellow);
                break;
            case GREEN:
                setTheme(R.style.AppTheme_Green);
                break;
            case BLUE:
                setTheme(R.style.AppTheme_Blue);
                break;
            case INDIGO:
                setTheme(R.style.AppTheme_Indigo);
                break;
            case VIOLET:
                setTheme(R.style.AppTheme_Violet);
                break;
        }
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
        loadData(false);
    }

    @Override
    public Event getData() {
        return event;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        //Log.i("event-activity", e.getMessage());
        if (e.getMessage().contains("404")) { // 404 Not Found
            Toast.makeText(this, getString(R.string.error_event_not_found), Toast.LENGTH_SHORT).show();
            onBackPressed();
            return getString(R.string.error_event_not_found);
        }
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        onBackPressed();


        return e.getMessage();
    }

    @Override
    public void setData(Event event) {
        this.event = event;

        /*
        // Event date
        date.setText(DateUtils.getRelativeTimeSpanString(event.getFromDate().toDate().getTime()));

        // Event Image
        Picasso.with(this).load(event.getImageUrl()).into(eventImg);

        // Event Time
        eventTime.setText(String.format(event.getFromDate().getHourOfDay()+"."+event.getFromDate().getMinuteOfHour()+ " - " + event.getToDate().getHourOfDay()+"."+event.getToDate().getMinuteOfHour()));


        // Event Place
        if (!event.getLocations().isEmpty()) {
            eventPlace.setText(event.getLocations().get(0).getName());
            for (int i = 0; i < event.getLocations().size(); i++) {
                locations.add(event.getLocations().get(i));
            }
        }
        */
        setTitle(event.getTitle());

        viewPager.setAdapter(new EventPagerAdapter(event, this, getSupportFragmentManager()));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.getEvent(eventId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "Edit event", Toast.LENGTH_SHORT).show();
                break;
            case R.id.map:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putParcelableArrayListExtra("loc", (ArrayList<? extends Parcelable>)event.getLocations());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Location item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(User item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }
}

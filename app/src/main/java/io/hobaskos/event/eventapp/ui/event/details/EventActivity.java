package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategoryTheme;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseLceViewStateActivity;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.CompetitionFragment;
import io.hobaskos.event.eventapp.ui.dialog.DeleteDialogFragment;
import io.hobaskos.event.eventapp.ui.dialog.DeleteDialogListener;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventActivity;
import io.hobaskos.event.eventapp.ui.event.details.attending.AttendeesFragment;
import io.hobaskos.event.eventapp.ui.event.details.competition.carousel.ImageCarouselActivity;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.OnCompetitionListInteractionListener;
import io.hobaskos.event.eventapp.ui.event.details.location.LocationsFragment;
import io.hobaskos.event.eventapp.ui.event.details.map.MapsActivity;
import io.hobaskos.event.eventapp.ui.location.add.LocationActivity;
import rx.Observer;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseLceViewStateActivity<RelativeLayout, Event, EventView,
        EventPresenter> implements
        EventView,
        LocationsFragment.OnListFragmentInteractionListener,
        AttendeesFragment.OnUserListFragmentInteractionListener,
        OnCompetitionListInteractionListener,
        DeleteDialogListener<Location> {

    public static final String ACTIVITY_STATE = "activity_state";
    public static final String EVENT = "event";
    public final static String EVENT_ID = "eventId";
    public final static String EVENT_THEME = "eventTheme";
    public final static String TAG = EventActivity.class.getName();

    private Long eventId;
    private EventPagerAdapter eventPagerAdapter;
    protected ViewPager viewPager;
    protected TabLayout tabLayout;
    private boolean isOwner = false;
    private boolean isLoggedIn;
    private Event event;
    private boolean hasBeenPaused = false;
    private Menu menu;

    @Inject public EventPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.loading);

        if(savedInstanceState == null) {

            Log.i("EventActivity", "Inside onCreate with savedInstanceState == null");

            EventCategoryTheme theme =
                    (EventCategoryTheme) getIntent().getExtras().getSerializable(EVENT_THEME);

            if (theme != null) { setEventTheme(theme); }

            eventId = getIntent().getExtras().getLong(EVENT_ID);


        } else {

            // The Activity was restarted
            Log.i("EventActivity", "Inside onCreate with savedInstanceState != null");

            try {

                EventCategoryTheme theme =
                        EventCategoryTheme.valueOf(savedInstanceState.get(EVENT_THEME).toString());

                setEventTheme(theme);

            } catch (NullPointerException e) {

                Log.i(TAG, e.getMessage());

            }

            eventId = savedInstanceState.getLong(EVENT_ID);
        }

        presenter.getEvent(eventId);

        setContentView(R.layout.activity_event);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isLoggedIn = presenter.isLoggedIn();
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart method called.");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop method called.");
        eventPagerAdapter = null;
        viewPager.setAdapter(null);
        super.onStop();
    }

    private void setEventTheme(EventCategoryTheme theme) {
        Log.i("EventActivity", "Setting EventTheme: " + theme.name());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("EventActivity", "Inside onSaveInstanceState");
        if(event != null) {
            Log.i("EventActivity", "Inside onSaveInstanceState. Event != null");
            outState.putLong(EVENT_ID, event.getId());
            outState.putString(EVENT_THEME, event.getCategory().getTheme().toString());
        }
    }

    @NonNull
    @Override
    public EventPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
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
        presenter.getOwnerStatus(event);
        setTitle(event.getTitle());

        Log.i(TAG, "setData");
        eventPagerAdapter = new EventPagerAdapter(event, isOwner, isLoggedIn, this, getSupportFragmentManager());
        viewPager.setAdapter(eventPagerAdapter);
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
        this.menu = menu;

        if(!hasBeenPaused && isOwner) {
            // Edit Event button
            menu.getItem(0).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                Intent editIntent = new Intent(this, CreateEventActivity.class);
                editIntent.putExtra(ACTIVITY_STATE, 1);
                editIntent.putExtra(EVENT, event);
                startActivity(editIntent);
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
    public void onListFragmentEditInteraction(Location item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LocationActivity.class);
        intent.putExtra(LocationActivity.EVENT_STATE, 1);
        intent.putExtra(LocationActivity.LOCATION, item);
        startActivity(intent);
    }

    @Override
    public void onListFragmentDeleteInteraction(Location item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        DeleteDialogFragment<Location> deleteDialog = new DeleteDialogFragment<>();
        deleteDialog.setItem(item);
        deleteDialog.show(getFragmentManager(), "EventActivity");
    }


    @Override
    public void onListFragmentInteraction(User item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setIsOwner(boolean owner) {
        isOwner = owner;

        if(!hasBeenPaused && isOwner) {
            menu.getItem(0).setVisible(true);
        }

        Toast.makeText(this, owner ? "Owner" : "Not owner", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        hasBeenPaused = true;
    }

    protected void onResume() {
        super.onResume();

        if(hasBeenPaused) {
            Log.i("EventActivity", "onResume(): hasBeenPaused==true");
            refresh();
        }
    }

    private void refresh() {
        Log.i("EventActivity", "Refreshing activity...");
        recreate();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onDeleteButtonClicked(Location location) {
        presenter.remove(location, new Observer<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Void aVoid) {
                presenter.getEvent(eventId, new Observer<Event>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Event event) {
                        Toast.makeText(EventActivity.this, "Location is removed", Toast.LENGTH_SHORT).show();
                        LocationsFragment locationsFragment = (LocationsFragment) eventPagerAdapter.getItem(1);
                        locationsFragment.refresh( (ArrayList<Location>) event.getLocations());
                    }
                });
            }
        });
    }

    @Override
    public void onCancelButtonClicked() {

    }

    @Override
    public void onListFragmentInteraction(Long id) {
        Log.i(TAG, "Clicked on image number " + id);
        Intent intent = new Intent(this, ImageCarouselActivity.class);
        intent.putExtra(ImageCarouselActivity.ARG_STARTING_COMPETION_IMAGE, id);
        intent.putExtra(ImageCarouselActivity.ARG_EVENT_ID, eventId);
        startActivity(intent);
    }
}

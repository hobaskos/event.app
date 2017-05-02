package io.hobaskos.event.eventapp.ui.event.details;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.eventbus.EventHasUpdatedLocations;
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
import io.hobaskos.event.eventapp.ui.event.details.info.EventInfoFragment;
import io.hobaskos.event.eventapp.ui.event.details.location.LocationsFragment;
import io.hobaskos.event.eventapp.ui.event.details.location.create.CreateLocationActivity;
import io.hobaskos.event.eventapp.ui.event.details.map.EventMapActivity;
import rx.Observer;
import rx.functions.Action1;

/**
 * Created by andre on 1/26/2017.
 */
public class EventActivity extends BaseLceViewStateActivity<RelativeLayout, Event, EventView,
        EventPresenter> implements
        EventView,
        LocationsFragment.OnListFragmentInteractionListener,
        AttendeesFragment.OnAttendeesInteractionListener,
        OnCompetitionListInteractionListener,
        DeleteDialogListener<Location> {

    public final static String TAG = EventActivity.class.getName();

    public static final String ACTIVITY_STATE = "activity_state";
    public static final String EVENT = "event";
    public final static String EVENT_ID = "eventId";
    public final static String EVENT_THEME = "eventTheme";
    public final static String EVENT_CREATED = "eventCreated";

    // States
    public static final int EDIT_EVENT_REQUEST = 1;
    public static final int ADD_LOCATION_REQUEST = 2;
    public static final int EDIT_LOCATION_REQUEST = 3;
    public static final int VIEW_COMPETITION_CAROUSEL = 4;

    private Long eventId;
    private EventCategoryTheme theme;
    private boolean isAttending;

    private EventPagerAdapter eventPagerAdapter;
    protected ViewPager viewPager;
    protected TabLayout tabLayout;
    private boolean isOwner = false;
    private boolean isLoggedIn;
    private Event event;
    private boolean hasBeenPaused = false;
    private Menu menu;

    @Inject protected EventPresenter presenter;
    @Inject protected AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.loading);
        boolean newEvent = false;

        if (savedInstanceState != null) {
            // The Activity was restarted
            try {
                eventId = savedInstanceState.getLong(EVENT_ID);
                theme = EventCategoryTheme.valueOf(savedInstanceState.get(EVENT_THEME).toString());
                setEventTheme(theme);
            } catch (NullPointerException e) {
                Log.i(TAG, e.getMessage());
            }

        } else {
            // The Activity is newly started from CreateEventActivity or the ListActivity.
            eventId = getIntent().getExtras().getLong(EVENT_ID);
            theme = (EventCategoryTheme) getIntent().getExtras().getSerializable(EVENT_THEME);
            newEvent = getIntent().getExtras().getBoolean(EVENT_CREATED);
            if (theme != null) { setEventTheme(theme); }

        }

        presenter.getEvent(eventId);

        setContentView(R.layout.activity_event);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isLoggedIn = presenter.isLoggedIn();
        viewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (newEvent) {
            Intent intent = new Intent(this, CreateLocationActivity.class);
            intent.putExtra(CreateLocationActivity.EVENT_ID, eventId);
            startActivityForResult(intent, EventActivity.ADD_LOCATION_REQUEST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onUserHasLoggedInEvent(EventHasUpdatedLocations eventHasUpdatedLocations) {
        event.setLocations(eventHasUpdatedLocations.getLocations());
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

    private void onLeaveEvent() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.leave_event)
                .setMessage(R.string.leave_event_desc)
                .setPositiveButton(R.string.leave_event, (dialog, which) ->
                    presenter.leaveEvent(event.getMyAttendance().getId(), aBoolean -> {
                        if (aBoolean) { onLeftEvent(); }
                    })
                )
                .setNegativeButton(R.string.close, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void onLeftEvent() {
        Toast.makeText(this, R.string.left_event, Toast.LENGTH_SHORT).show();
        menu.findItem(R.id.leave_event).setVisible(false);
        loadData(true);

        eventPagerAdapter.getCompetitionFragment().setAttendingEvent(false);
        eventPagerAdapter.getAttendeesFragment().onLeftEvent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("EventActivity", "Inside onSaveInstanceState");

        if(eventId > 0 && theme != null) {
            outState.putLong(EVENT_ID, eventId);
            outState.putString(EVENT_THEME, theme.toString());
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
        isAttending = event.isAttending();

        if (isAttending) {
            menu.findItem(R.id.leave_event).setVisible(true);
        }

        setTitle(event.getTitle());

        Log.i(TAG, "setData");

        if (eventPagerAdapter == null) {
            Log.i(TAG, "eventPagerAdapter == null");
            eventPagerAdapter = new EventPagerAdapter(this.event, isOwner, isLoggedIn, this, getSupportFragmentManager());
            viewPager.setAdapter(eventPagerAdapter);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_event);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_location_on);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_group);

            if (isLoggedIn) { tabLayout.getTabAt(3).setIcon(R.drawable.trophy); }
        } else {
            eventPagerAdapter.getEventInfoFragment().refresh(event);
        }
    }

    @Override
    public void reloadData(Event event) {
        this.event = event;
        theme = event.getCategory().getTheme();
        if (theme != null) { setEventTheme(theme); }
        EventInfoFragment fragment = (EventInfoFragment) eventPagerAdapter.getItem(EventPagerAdapter.EVENT_INFO_FRAGMENT);
        fragment.refresh(event);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.getEvent(eventId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        this.menu = menu;

        if (!hasBeenPaused && isOwner) {
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
                //startActivity(editIntent);
                startActivityForResult(editIntent, EDIT_EVENT_REQUEST);
                break;
            case R.id.leave_event:
                onLeaveEvent();
                break;
            case R.id.map:
                Intent intent = new Intent(this, EventMapActivity.class);
                intent.putParcelableArrayListExtra(EventMapActivity.LOCATIONS, (ArrayList<? extends Parcelable>)event.getLocations());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
        if (isOwner && menu != null) { menu.getItem(0).setVisible(true); }
    }

    @Override
    public void onDeleteDialogConfirmButtonClicked(Location location) {
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
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(Event event) {
                        Toast.makeText(EventActivity.this, "Location is removed", Toast.LENGTH_SHORT).show();
                        LocationsFragment locationsFragment = (LocationsFragment) eventPagerAdapter.getItem(1);
                        locationsFragment.loadData(true);
                    }
                });
            }
        });
    }

    @Override
    public void onDeleteDialogCancelButtonClicked() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: resultCode: " + resultCode + ", requestCode: " + requestCode);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case EDIT_EVENT_REQUEST:
                Log.i(TAG, "activityForResult with request code == EDIT_EVENT_REQUEST");
                presenter.reloadEvent(eventId);
                break;

            case EDIT_LOCATION_REQUEST:
                Log.i(TAG, "activityForResult with request code == EDIT_LOCATION_REQUEST");
                LocationsFragment fragment = (LocationsFragment)eventPagerAdapter.getItem(EventPagerAdapter.LOCATIONS_FRAGMENT);
                fragment.loadData(true);
                break;

            case VIEW_COMPETITION_CAROUSEL:
                Log.i(TAG, "activityForResult with request code == VIEW_COMPETITION_CAROUSEL");
                break;

            default:
                Log.i(TAG, "activityForResult without legal request code.");
                // To something ??
                break;
        }
    }

    @Override
    public void onCompetitionVoteButtonClicked(CompetitionImage competitionImage, int vote) {
        CompetitionFragment competitionFragment =
                (CompetitionFragment) eventPagerAdapter.getItem(EventPagerAdapter.COMPETITIONS_FRAGMENT);

        if (!isAttending) {
            AttendeesFragment attendeesFragment =
                    (AttendeesFragment) eventPagerAdapter.getItem(EventPagerAdapter.ATTENDEES_FRAGMENT);
            attendeesFragment.attendEvent((bool) -> {
                if (bool) {
                    isAttending = true;
                    competitionFragment.onCompetitionImageVoteSubmitted(competitionImage, vote);
                }
            });
        } else {
            competitionFragment.onCompetitionImageVoteSubmitted(competitionImage, vote);
        }
    }

    @Override
    public void onCompetitionImageClick(CompetitionImage competitionImage) {
        Intent intent = new Intent(this, ImageCarouselActivity.class);
        intent.putExtra(ImageCarouselActivity.ARG_COMPETITION_ID, event.getDefaultPollId());
        intent.putExtra(ImageCarouselActivity.ARG_EVENT_TITLE, event.getTitle());
        intent.putExtra(ImageCarouselActivity.ARG_EVENT_GOING, isAttending);

        if (!isAttending) {
            AttendeesFragment attendeesFragment =
                    (AttendeesFragment) eventPagerAdapter.getItem(EventPagerAdapter.ATTENDEES_FRAGMENT);
            attendeesFragment.attendEvent((bool) -> {
                if (bool) {
                    isAttending = true;
                    startActivityForResult(intent, VIEW_COMPETITION_CAROUSEL);
                }
            });
        } else {
            startActivityForResult(intent, VIEW_COMPETITION_CAROUSEL);
        }
    }

    @Override
    public void onRequestForAttendingEvent(Action1<Boolean> callback) {
        AttendeesFragment attendeesFragment =
                    (AttendeesFragment) eventPagerAdapter.getItem(EventPagerAdapter.ATTENDEES_FRAGMENT);
        attendeesFragment.attendEvent(callback);
    }

    @Override
    public void onLocationMapInteraction(List<Location> locations, Location focus) {
        Intent intent = new Intent(this, EventMapActivity.class);
        intent.putExtra(EventMapActivity.FOCUS_POINT, true);
        intent.putExtra(EventMapActivity.FOCUS_LAT, focus.getGeoPoint().getLat());
        intent.putExtra(EventMapActivity.FOCUS_LNG, focus.getGeoPoint().getLon());
        intent.putParcelableArrayListExtra(EventMapActivity.LOCATIONS, (ArrayList<? extends Parcelable>)locations);
        startActivity(intent);
    }

    @Override
    public void onLocationEditInteraction(Location item) {
        Intent intent = new Intent(this, CreateLocationActivity.class);
        intent.putExtra(CreateLocationActivity.EVENT_STATE, 1);
        intent.putExtra(CreateLocationActivity.LOCATION, item);
        startActivityForResult(intent, EDIT_LOCATION_REQUEST);
    }

    @Override
    public void onLocationDeleteInteraction(Location item) {
        DeleteDialogFragment<Location> deleteDialog = new DeleteDialogFragment<>();
        deleteDialog.setItem(item);
        deleteDialog.show(getFragmentManager(), "EventActivity");
    }

    @Override
    public void onUserAttendingInteraction(User item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserHasAttendedEvent() {
        Log.i(TAG, "onUserHasAttendedEvent");
        CompetitionFragment competitionFragment =
                (CompetitionFragment) eventPagerAdapter.getItem(EventPagerAdapter.COMPETITIONS_FRAGMENT);
        competitionFragment.setAttendingEvent(true);
        loadData(true);
    }
}

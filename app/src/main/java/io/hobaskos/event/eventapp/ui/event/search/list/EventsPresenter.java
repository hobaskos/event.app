package io.hobaskos.event.eventapp.ui.event.search.list;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.eventbus.SetEventsEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.service.GPSService;
import io.hobaskos.event.eventapp.data.service.GPSTracker;
import io.hobaskos.event.eventapp.data.storage.FilterSettings;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsFragment;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsPresenter extends BaseRxLcePresenter<EventsView, List<Event>>  {

    public final static String TAG = EventsPresenter.class.getName();

    protected EventRepository eventRepository;

    private Subscriber<List<Event>> moreEventSubscriber;

    private FilterSettings filterSettings;

    private boolean usingCurrentLocation;

    private int distance;
    private double lat;
    private double lon;
    private long categoryId;

    private GPSService gpsService;
    private GPSTracker gpsTracker;

    @Override
    public void attachView(EventsView view) {
        super.attachView(view);

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    @Inject
    public EventsPresenter(EventRepository eventRepository,
                           FilterSettings filterSettings,
                           PersistentStorage persistentStorage) {
        this.eventRepository = eventRepository;
        this.filterSettings = filterSettings;

        //this.gpsService = new GPSService();
        this.gpsTracker = new GPSTracker(App.getInst().getApplicationContext());

    }

    public void loadEvents(boolean pullToRefresh, String searchQuery) {
        searchQuery = searchQuery + "*";

        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }
        // Load filter values (from shared preferences)
        loadFilterValues();

        DateTime fromDate = DateTime.now().minusDays(2);
        DateTime toDate = fromDate.plusYears(2);

        // If distance is higher than MAX_DISTANCE, set a high value for unlimited distance
        if (distance > FilterEventsFragment.MAX_DISTANCE) {
            distance = Integer.MAX_VALUE;
        }

        // Setup observable:
        Observable<List<Event>> observable = eventRepository.searchNearby(0, searchQuery, lat, lon, distance + "km", fromDate, toDate, categoryId + "");
        // setup and start subscription:
        subscribe(observable, pullToRefresh);
    }

    public void loadMoreEvents(int nextPage, String searchQuery) {
        searchQuery = searchQuery + "*";

        // Cancel any previous query
        unsubscribe();
        // Load filter values (from shared preferences)
        loadFilterValues();
        DateTime fromDate = DateTime.now();
        DateTime toDate = fromDate.plusYears(2);


        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.searchNearby(nextPage, searchQuery, lat, lon, distance + "km", fromDate, toDate, categoryId + "");

        Log.i(TAG, " Observable up");
        // Show loading in view:
        if (isViewAttached()) {
            getView().showLoadMore(true);
        }
        // Setup subscriber:
        moreEventSubscriber = new Subscriber<List<Event>>() {
            @Override public void onCompleted() {
            }
            @Override  public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }
            @Override public void onNext(List<Event> events) {
                if (isViewAttached()) {
                    getView().addMoreData(events);
                    getView().showLoadMore(false);
                }
            }
        };
        // start subscription:
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moreEventSubscriber);
    }

    @Override protected void unsubscribe() {
        super.unsubscribe();
        if (moreEventSubscriber != null && !moreEventSubscriber.isUnsubscribed()) {
            moreEventSubscriber.unsubscribe();
        }
    }

    @Override protected void onNext(List<Event> events) {
        super.onNext(events);
        EventBus.getDefault().postSticky(new SetEventsEvent(events));
        Log.d(TAG, "onNext()");
    }

    private void loadFilterValues() {
        distance = filterSettings.getDistance();
        lat = filterSettings.getPlaceLat();
        lon = filterSettings.getPlaceLon();
        categoryId = filterSettings.getCategoryId();

        usingCurrentLocation = filterSettings.isUsingCurrentLocation();


        // Check if "Use current location" is selcted
        if (usingCurrentLocation) {
            Log.i(TAG, " USE CURRENT LOCATION");
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
            Log.i(TAG, "lat, lon: " + lat + ", " + lon);

        } else {
            Log.i(TAG, " NOT USING CURRENT LOCATION");
        }
    }
}

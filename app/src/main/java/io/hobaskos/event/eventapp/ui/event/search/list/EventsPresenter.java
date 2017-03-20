package io.hobaskos.event.eventapp.ui.event.search.list;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.eventbus.SetEventsEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.service.GPSService;
import io.hobaskos.event.eventapp.data.storage.FilterSettings;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
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

    private PersistentStorage persistentStorage;
    private FilterSettings filterSettings;

    private boolean useCurrentLocation;

    private int distance;
    private double lat;
    private double lon;
    private long categoryId;

    private GPSService gpsService;

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
        this.persistentStorage = persistentStorage;

        this.gpsService = new GPSService();


    }

    public void loadEvents(boolean pullToRefresh, String searchQuery) {
        searchQuery = searchQuery + "*";


        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }
        // Load filter values (from shared preferences)
        loadFilterValues();

        DateTime fromDate = DateTime.now();
        DateTime toDate = fromDate.plusYears(2);

        Log.i(TAG, "Observable up");
        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.searchNearby(0, searchQuery, lat, lon, distance + "km", fromDate, toDate, categoryId + "");

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

        useCurrentLocation = filterSettings.getCurrentLocation();

        if (useCurrentLocation) {
            Log.i(TAG, " USE CURRENT LOCATION");
            lat = gpsService.lat;
            lon = gpsService.lon;

        } else {
            Log.i(TAG, " NOT USING CURRENT LOCATION");
        }
    }
}

package io.hobaskos.event.eventapp.ui.event.search.list;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.eventbus.FiltersUpdatedEvent;
import io.hobaskos.event.eventapp.data.eventbus.SetEventsEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsPresenter extends BaseRxLcePresenter<EventsView, List<Event>> {

    public final static String TAG = EventsPresenter.class.getName();

    protected EventRepository eventRepository;

    private Subscriber<List<Event>> moreEventSubscriber;

    private PersistentStorage persistentStorage;

    private int distance;
    private double lat;
    private double lon;
    private long categoryId;

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
                           PersistentStorage persistentStorage) {
        this.eventRepository = eventRepository;

        this.persistentStorage = persistentStorage;
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
        Log.d("aaaaaaaaaaaa", "HEEEI");
    }

    private void loadFilterValues() {
        distance = persistentStorage.getInt(FilterEventsPresenter.FILTER_EVENTS_DISTANCE_KEY, 10);
        lat = persistentStorage.getDouble(FilterEventsPresenter.FILTER_EVENTS_LOCATION_LAT_KEY, 0);
        lon = persistentStorage.getDouble(FilterEventsPresenter.FILTER_EVENTS_LOCATION_LON_KEY, 0);
        categoryId = persistentStorage.getLong(FilterEventsPresenter.FILTER_EVENTS_CATEGORY_KEY, 0);
    }
}

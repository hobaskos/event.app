package io.hobaskos.event.eventapp.ui.event.list;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsPresenter extends BaseRxLcePresenter<EventsView, List<EventsPresentationModel>> {

    protected EventRepository eventRepository;

    private Subscriber<List<EventsPresentationModel>> moreEventSubscriber;

    private Func1<List<Event>, List<EventsPresentationModel>> presentationModelTransformation;

    PersistentStorage persistentStorage;

    private int distance;
    private double lat;
    private double lon;

    @Inject
    public EventsPresenter(EventRepository eventRepository, PersistentStorage persistentStorage) {
        this.eventRepository = eventRepository;
        presentationModelTransformation = events -> {
            List<EventsPresentationModel> pmEvents = new ArrayList<>();
            for (Event event : events) {
                EventsPresentationModel pm = new EventsPresentationModel(event);
                pmEvents.add(pm);
            }
            return pmEvents;
        };

        this.persistentStorage = persistentStorage;
    }

    public void loadEvents(boolean pullToRefresh) {
        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }
        // Load filter values (from shared preferences)
        loadFilterValues();

        DateTime fromDate = DateTime.now();
        DateTime toDate = fromDate.plusYears(2);

        // Setup observable:
        final Observable<List<EventsPresentationModel>> observable =
                eventRepository.searchNearby(0, lat, lon, distance + "km", fromDate, toDate)
                        .map(presentationModelTransformation);

        // setup and start subscription:
        subscribe(observable, pullToRefresh);
    }

    public void loadMoreEvents(int nextPage) {
        // Cancel any previous query
        unsubscribe();
        // Load filter values (from shared preferences)
        loadFilterValues();
        DateTime fromDate = DateTime.now();
        DateTime toDate = fromDate.plusYears(2);
        // Setup observable:
        final Observable<List<EventsPresentationModel>> observable =
                eventRepository.searchNearby(nextPage, lat, lon, distance + "km", fromDate, toDate)
                        .map(presentationModelTransformation);
        // Show loading in view:
        if (isViewAttached()) {
            getView().showLoadMore(true);
        }
        // Setup subscriber:
        moreEventSubscriber = new Subscriber<List<EventsPresentationModel>>() {
            @Override public void onCompleted() {
            }
            @Override  public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }
            @Override public void onNext(List<EventsPresentationModel> events) {
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

    private void loadFilterValues() {
        distance = persistentStorage.getInt(FilterEventsPresenter.FILTER_EVENTS_DISTANCE_KEY, 10);
        lat = persistentStorage.getDouble(FilterEventsPresenter.FILTER_EVENTS_LOCATION_LAT_KEY, 0);
        lon = persistentStorage.getDouble(FilterEventsPresenter.FILTER_EVENTS_LOCATION_LON_KEY, 0);
    }


}

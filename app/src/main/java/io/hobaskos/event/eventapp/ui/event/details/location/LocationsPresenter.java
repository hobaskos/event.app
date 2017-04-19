package io.hobaskos.event.eventapp.ui.event.details.location;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.repository.LocationRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 4/19/17.
 */
public class LocationsPresenter extends BaseRxLcePresenter<LocationsView, List<Location>> {

    public static final String TAG = LocationsPresenter.class.getName();

    private LocationRepository locationRepository;

    private Subscriber<List<Location>> moreLocationsObserver;

    @State
    protected Long eventId;

    @Inject
    public LocationsPresenter(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void loadLocations(boolean pullToRefresh, Long eventId) {
        Log.d(TAG, "loadLocations: ");

        this.eventId = eventId;

        if (isViewAttached()) { getView().showLoadMore(false); }
        subscribe(locationRepository.getLocationsForEvent(eventId, 0), pullToRefresh);

    }

    public void loadMoreLocations(int page) {
        Log.d(TAG, "loadMoreLocations: ");

        if (isViewAttached()) { getView().showLoadMore(false); }
        unsubscribe();

        if (moreLocationsObserver != null && !moreLocationsObserver.isUnsubscribed()) {
            moreLocationsObserver.unsubscribe();
        }

        moreLocationsObserver = new Subscriber<List<Location>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                getView().showLoadMoreError(e);
                getView().showLoadMore(false);
            }

            @Override
            public void onNext(List<Location> users) {
                if (isViewAttached()) {
                    getView().addMoreData(users);
                    getView().showLoadMore(false);
                }
            }
        };
        locationRepository.getLocationsForEvent(eventId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moreLocationsObserver);
    }
}

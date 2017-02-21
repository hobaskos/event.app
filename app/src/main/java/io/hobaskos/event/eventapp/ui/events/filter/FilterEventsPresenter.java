package io.hobaskos.event.eventapp.ui.events.filter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.PersistentStorage;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsPresenter extends MvpBasePresenter<FilterEventsView> {

    private PersistentStorage persistentStorage;

    public static final String FILTER_EVENTS_DISTANCE_KEY = "filter_events_distance_key";

    @Inject
    public FilterEventsPresenter(PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }

    public void storeDistance(int distance) {
        persistentStorage.putInt(FILTER_EVENTS_DISTANCE_KEY, distance);
    }

    public void loadDistance() {
        getView().setDistance(persistentStorage.getInt(FILTER_EVENTS_DISTANCE_KEY, 10));
    }

}

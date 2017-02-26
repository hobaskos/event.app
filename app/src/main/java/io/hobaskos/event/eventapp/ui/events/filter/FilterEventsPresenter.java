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
    public static final String FILTER_EVENTS_LOCATION_NAME_KEY = "filter_events_location_name_key";
    public static final String FILTER_EVENTS_LOCATION_LAT_KEY = "filter_events_location_lat_key";
    public static final String FILTER_EVENTS_LOCATION_LON_KEY = "filter_events_location_lon_key";

    @Inject
    public FilterEventsPresenter(PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }

    public void storeDistance(int distance) {
        persistentStorage.putInt(FILTER_EVENTS_DISTANCE_KEY, distance);
    }

    public void storeLocation(String name, double lat, double lon) {
        persistentStorage.put(FILTER_EVENTS_LOCATION_NAME_KEY, name);
        persistentStorage.putDouble(FILTER_EVENTS_LOCATION_LAT_KEY, lat);
        persistentStorage.putDouble(FILTER_EVENTS_LOCATION_LON_KEY, lon);
    }

    public void loadLocation() {
        String name = persistentStorage.get(FILTER_EVENTS_LOCATION_NAME_KEY);
        double lat = persistentStorage.getDouble(FILTER_EVENTS_LOCATION_LAT_KEY, 0);
        double lon = persistentStorage.getDouble(FILTER_EVENTS_LOCATION_LAT_KEY, 0);
        getView().setLocation(name, lat, lon);
    }

    public void loadDistance() {
        getView().setDistance(persistentStorage.getInt(FILTER_EVENTS_DISTANCE_KEY, 10));
    }

}

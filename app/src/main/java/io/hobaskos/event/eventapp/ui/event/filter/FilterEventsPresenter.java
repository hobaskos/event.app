package io.hobaskos.event.eventapp.ui.event.filter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;

/**
 * Created by andre on 2/20/2017.
 */

public class FilterEventsPresenter extends MvpBasePresenter<FilterEventsView> {

    private PersistentStorage persistentStorage;

    private EventRepository eventRepository;
    private EventCategoryRepository eventCategoryRepository;

    public static final String FILTER_EVENTS_DISTANCE_KEY = "filter_events_distance_key";
    public static final String FILTER_EVENTS_LOCATION_NAME_KEY = "filter_events_location_name_key";
    public static final String FILTER_EVENTS_LOCATION_LAT_KEY = "filter_events_location_lat_key";
    public static final String FILTER_EVENTS_LOCATION_LON_KEY = "filter_events_location_lon_key";

    @Inject
    public FilterEventsPresenter(PersistentStorage persistentStorage,
                                 EventRepository eventRepository,
                                 EventCategoryRepository eventCategoryRepository) {
        this.persistentStorage = persistentStorage;
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
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

    public void loadCategories() {
        //eventCategoryRepository.getAll();
        getView().loadCategories(null);
    }

}

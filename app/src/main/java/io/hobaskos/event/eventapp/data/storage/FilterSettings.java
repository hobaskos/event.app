package io.hobaskos.event.eventapp.data.storage;

import javax.inject.Inject;

/**
 * Created by test on 3/14/2017.
 */

public class FilterSettings {

    private final PersistentStorage persistentStorage;

    @Inject
    public FilterSettings(PersistentStorage persistentStorage)
    {
        this.persistentStorage = persistentStorage;
    }

    public static final String FILTER_EVENTS_DISTANCE_KEY = "filter_events_distance_key";
    public static final String FILTER_EVENTS_LOCATION_NAME_KEY = "filter_events_location_name_key";
    public static final String FILTER_EVENTS_LOCATION_LAT_KEY = "filter_events_location_lat_key";
    public static final String FILTER_EVENTS_LOCATION_LON_KEY = "filter_events_location_lon_key";
    public static final String FILTER_EVENTS_CATEGORY_KEY = "filter_events_category_key";

    public static final int DEFAULT_DISTANCE = 10;
    public static final String DEFAULT_PLACE_NAME = "";
    public static final double DEFAULT_PLACE_LAT = 0;
    public static final double DEFAULT_PLACE_LON = 0;
    public static final long DEFAULT_CATEGORY_ID = 0;

    public int getDistance() {
        return persistentStorage.getInt(FILTER_EVENTS_DISTANCE_KEY, DEFAULT_DISTANCE);
    }

    public void putDistance(int distance) {
        persistentStorage.putInt(FILTER_EVENTS_DISTANCE_KEY, distance);
    }

    public String getPlaceName() {
        return persistentStorage.get(FILTER_EVENTS_LOCATION_NAME_KEY);
    }

    public void putPlaceName(String name) {
        persistentStorage.put(FILTER_EVENTS_LOCATION_NAME_KEY, name);
    }

    public double getPlaceLat() {
        return persistentStorage.getDouble(FILTER_EVENTS_LOCATION_LAT_KEY, DEFAULT_PLACE_LAT);
    }

    public void putPlaceLat(double lat) {
        persistentStorage.putDouble(FILTER_EVENTS_LOCATION_LAT_KEY, lat);
    }


    public double getPlaceLon() {
        return persistentStorage.getDouble(FILTER_EVENTS_LOCATION_LON_KEY, DEFAULT_PLACE_LON);
    }

    public void putPlaceLon(double lon) {
        persistentStorage.putDouble(FILTER_EVENTS_LOCATION_LON_KEY, lon);
    }

    public long getCategoryId() {
        return persistentStorage.getLong(FILTER_EVENTS_CATEGORY_KEY, DEFAULT_CATEGORY_ID);
    }

    public void putCategoryId(long categoryId) {
        persistentStorage.putLong(FILTER_EVENTS_CATEGORY_KEY, categoryId);
    }
}

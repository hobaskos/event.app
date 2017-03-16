package io.hobaskos.event.eventapp.util;

import com.google.android.gms.maps.model.LatLng;

import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by test on 3/13/2017.
 */

public final class LocationUtil {

    public static LatLng LocationToLatLng(Location location) {
        GeoPoint geoPoint = location.getGeoPoint();
        return new LatLng(geoPoint.getLat(), geoPoint.getLon());
    }
}

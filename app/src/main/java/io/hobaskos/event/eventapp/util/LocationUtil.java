package io.hobaskos.event.eventapp.util;

import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.hobaskos.event.eventapp.data.model.GeoPoint;
import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by test on 3/13/2017.
 */

public final class LocationUtil {

    public static LatLng locationToLatLng(Location location) {
        GeoPoint geoPoint = location.getGeoPoint();
        return new LatLng(geoPoint.getLat(), geoPoint.getLon());
    }

}

package io.hobaskos.event.eventapp.data.model;

/**
 * Created by osvold.hans.petter on 08.02.2017.
 */

public class GeoPoint {

    private int lat;
    private int lon;

    public GeoPoint(int lat, int lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }
}

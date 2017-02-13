package io.hobaskos.event.eventapp.data.model;

/**
 * Created by osvold.hans.petter on 08.02.2017.
 */

public class GeoPoint {

    private double lat;
    private double lon;

    public GeoPoint(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}

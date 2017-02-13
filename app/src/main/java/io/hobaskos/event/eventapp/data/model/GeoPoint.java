package io.hobaskos.event.eventapp.data.model;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by osvold.hans.petter on 08.02.2017.
 */

@Parcel
public class GeoPoint implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.lat);
        dest.writeInt(this.lon);
    }

    protected GeoPoint(android.os.Parcel in) {
        this.lat = in.readInt();
        this.lon = in.readInt();
    }

    public static final Parcelable.Creator<GeoPoint> CREATOR = new Parcelable.Creator<GeoPoint>() {
        @Override
        public GeoPoint createFromParcel(android.os.Parcel source) {
            return new GeoPoint(source);
        }

        @Override
        public GeoPoint[] newArray(int size) {
            return new GeoPoint[size];
        }
    };
}

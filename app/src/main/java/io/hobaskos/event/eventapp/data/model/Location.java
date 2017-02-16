package io.hobaskos.event.eventapp.data.model;

<<<<<<< HEAD
import android.os.Parcelable;

=======
import android.util.EventLogTags;

import org.joda.time.DateTime;
>>>>>>> #FRONTEND-7
import org.joda.time.LocalDateTime;
import org.parceler.Parcel;

/**
 * Created by osvold.hans.petter on 08.02.2017.
 */
@Parcel
public class Location implements Parcelable {

    private long id;
    private String name;
    private String description;
    private GeoPoint geoPoint;
    private int vector;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private int eventId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public int getVector() {
        return vector;
    }

    public void setVector(int vector) {
        this.vector = vector;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeParcelable(this.geoPoint, flags);
        dest.writeInt(this.vector);
        dest.writeSerializable(this.fromDate);
        dest.writeSerializable(this.toDate);
        dest.writeInt(this.eventId);
    }

    public Location() {
    }

    protected Location(android.os.Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.geoPoint = in.readParcelable(GeoPoint.class.getClassLoader());
        this.vector = in.readInt();
        this.fromDate = (LocalDateTime) in.readSerializable();
        this.toDate = (LocalDateTime) in.readSerializable();
        this.eventId = in.readInt();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(android.os.Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public String toString() {
        return getName();
    }

}

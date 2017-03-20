package io.hobaskos.event.eventapp.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by osvold.hans.petter on 08.02.2017.
 */
public class Location implements Parcelable {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("geoPoint")
    private GeoPoint geoPoint;
    @SerializedName("fromDate")
    private DateTime fromDate;
    @SerializedName("toDate")
    private DateTime toDate;
    @SerializedName("eventId")
    private Long eventId;
    @SerializedName("address")
    private String address;
    @SerializedName("searchName")
    private String searchName;

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

    public DateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getDateLine(Context context) {
        if(fromDate == null || toDate == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE))
                    .append("  ")
                    .append(DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_TIME))
                    .append("  ->  ")
                    .append(DateUtils.formatDateTime(context, toDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE))
                    .append("  ")
                    .append(DateUtils.formatDateTime(context, toDate.toDate().getTime(), DateUtils.FORMAT_SHOW_TIME));

        return stringBuilder.toString();
    }

    /**
     * Checks if this location is currently on going.
     * @return true if location is ongoing, false otherwise.
     */
    public boolean isOnGoing() {

        DateTime fromDate = this.getFromDate();
        DateTime toDate = this.getToDate();

        if (fromDate.isBeforeNow() && toDate.isBeforeNow()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeParcelable(this.geoPoint, flags);
        dest.writeSerializable(this.fromDate);
        dest.writeSerializable(this.toDate);
        dest.writeValue(this.eventId);
        dest.writeString(this.address);
        dest.writeString(this.searchName);
    }

    public Location() {
    }

    protected Location(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.geoPoint = in.readParcelable(GeoPoint.class.getClassLoader());
        this.fromDate = (DateTime) in.readSerializable();
        this.toDate = (DateTime) in.readSerializable();
        this.eventId = (Long) in.readValue(Long.class.getClassLoader());
        this.address = in.readString();
        this.searchName = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (id != null ? !id.equals(location.id) : location.id != null) return false;
        if (name != null ? !name.equals(location.name) : location.name != null) return false;
        if (description != null ? !description.equals(location.description) : location.description != null)
            return false;
        if (geoPoint != null ? !geoPoint.equals(location.geoPoint) : location.geoPoint != null)
            return false;
        if (fromDate != null ? !fromDate.equals(location.fromDate) : location.fromDate != null)
            return false;
        if (toDate != null ? !toDate.equals(location.toDate) : location.toDate != null)
            return false;
        if (eventId != null ? !eventId.equals(location.eventId) : location.eventId != null)
            return false;
        if (address != null ? !address.equals(location.address) : location.address != null)
            return false;
        return searchName != null ? searchName.equals(location.searchName) : location.searchName == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (geoPoint != null ? geoPoint.hashCode() : 0);
        result = 31 * result + (fromDate != null ? fromDate.hashCode() : 0);
        result = 31 * result + (toDate != null ? toDate.hashCode() : 0);
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (searchName != null ? searchName.hashCode() : 0);
        return result;
    }
}

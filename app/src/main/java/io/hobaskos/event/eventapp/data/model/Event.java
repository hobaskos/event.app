package io.hobaskos.event.eventapp.data.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * Created by andre on 1/25/2017.
 */
public class Event implements Parcelable {

    @SerializedName("id")
    public Long id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    private String description;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("fromDate")
    private LocalDateTime fromDate;

    @SerializedName("toDate")
    private LocalDateTime toDate;

    @SerializedName("ownerId")
    private long ownerId;

    @SerializedName("locations")
    private List<Location> locations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeSerializable(this.fromDate);
        dest.writeSerializable(this.toDate);
        dest.writeLong(this.ownerId);
        dest.writeList(this.locations);
    }

    public Event() {
    }

    protected Event(android.os.Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.fromDate = (LocalDateTime) in.readSerializable();
        this.toDate = (LocalDateTime) in.readSerializable();
        this.ownerId = in.readLong();
        in.readList(this.locations, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(android.os.Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}

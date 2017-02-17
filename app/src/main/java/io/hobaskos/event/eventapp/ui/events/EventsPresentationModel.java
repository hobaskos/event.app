package io.hobaskos.event.eventapp.ui.events;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import org.joda.time.LocalDateTime;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 2/16/2017.
 */

/**
 * A Events Presentation Model
 */
public class EventsPresentationModel implements Parcelable {

    public Long id;
    public String title;
    private String description;
    private String imageUrl;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String date;
    private long ownerId;
    private String location;

    public EventsPresentationModel(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.imageUrl = event.getImageUrl();
        this.ownerId = event.getOwnerId();
        this.fromDate = event.getFromDate();
        this.toDate = event.getToDate();
        this.date = DateUtils.formatDateTime(App.getInst(),
                fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
        if (event.getLocations() != null && !event.getLocations().isEmpty()) {
            this.location = event.getLocations().get(0).getName();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeSerializable(this.fromDate);
        dest.writeSerializable(this.toDate);
        dest.writeLong(this.ownerId);
        dest.writeString(this.location);
    }

    protected EventsPresentationModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.fromDate = (LocalDateTime) in.readSerializable();
        this.toDate = (LocalDateTime) in.readSerializable();
        this.ownerId = in.readLong();
        this.location = in.readString();
    }

    public static final Parcelable.Creator<EventsPresentationModel> CREATOR = new Parcelable.Creator<EventsPresentationModel>() {
        @Override
        public EventsPresentationModel createFromParcel(Parcel source) {
            return new EventsPresentationModel(source);
        }

        @Override
        public EventsPresentationModel[] newArray(int size) {
            return new EventsPresentationModel[size];
        }
    };
}

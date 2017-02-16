package io.hobaskos.event.eventapp.data.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Date;
import java.util.Set;

/**
 * Created by andre on 1/25/2017.
 */
public class Event {

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
    private Set<Location> locations;

    @SerializedName("eventCategory")
    private EventCategory category;

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

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public EventCategory getEventCategory() {
        return category;
    }
}

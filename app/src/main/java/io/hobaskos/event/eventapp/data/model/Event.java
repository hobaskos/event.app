package io.hobaskos.event.eventapp.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;

import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;

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
    private DateTime fromDate;

    @SerializedName("toDate")
    private DateTime toDate;

    @SerializedName("ownerLogin")
    private String ownerLogin;

    @SerializedName("locations")
    private List<Location> locations;

    @SerializedName("eventCategory")
    private EventCategory category;

    @SerializedName("attendanceCount")
    private int attendanceCount;

    @SerializedName("myAttendance")
    private EventAttendingType myAttendance;

    @SerializedName("image")
    private String image;

    @SerializedName("imageContentType")
    private String imageContentType;

    private boolean privateEvent;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setContentType(String contentType) {
        this.imageContentType = contentType;
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

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public EventCategory getEventCategory() {
        return category;
    }

    public int getAttendanceCount() {
        return attendanceCount;
    }

    public void setAttendanceCount(int attendanceCount) {
        this.attendanceCount = attendanceCount;
    }

    public EventAttendingType getMyAttendance() {
        return myAttendance;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public void setMyAttendance(EventAttendingType myAttendance) {
        this.myAttendance = myAttendance;
    }

    public String getDate(Context context) {
        return fromDate != null ? DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE) : "";
    }

    public String getLocation() {
        return locations.size() > 0 ? locations.get(0).getName() : "";
    }

    public Event() {
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
        dest.writeString(this.ownerLogin);
        dest.writeTypedList(this.locations);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.attendanceCount);
        dest.writeInt(this.myAttendance == null ? -1 : this.myAttendance.ordinal());
    }

    protected Event(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.fromDate = (DateTime) in.readSerializable();
        this.toDate = (DateTime) in.readSerializable();
        this.ownerLogin = in.readString();
        this.locations = in.createTypedArrayList(Location.CREATOR);
        this.category = in.readParcelable(EventCategory.class.getClassLoader());
        this.attendanceCount = in.readInt();
        int tmpMyAttendance = in.readInt();
        this.myAttendance = tmpMyAttendance == -1 ? null : EventAttendingType.values()[tmpMyAttendance];
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", ownerLogin=" + ownerLogin +
                ", locations=" + locations +
                ", category=" + category +
                ", attendanceCount=" + attendanceCount +
                ", myAttendance=" + myAttendance +
                ", imageContentType='" + imageContentType + '\'' +
                ", privateEvent=" + privateEvent +
                '}';
    }
}

package io.hobaskos.event.eventapp.data.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.hobaskos.event.eventapp.config.Constants;
import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;

/**
 * Created by andre on 1/25/2017.
 */
public class Event implements Parcelable {

    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

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

    @SerializedName("defaultPollId")
    private int defaultPollId;

    private boolean privateEvent;

    private String invitationCode;

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

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
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

    public Long getDefaultPollId() {
        return new Long(defaultPollId);
    }

    public void setDefaultPollId(int defaultPollId) {
        this.defaultPollId = defaultPollId;
    }

    public String getDate(Context context) {
        String dateString = "";
        if (fromDate != null) {
            dateString += DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_WEEKDAY); // Add Weekday
            dateString += ", ";
            dateString += DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE); // Add date
            dateString += " @ ";
            dateString += DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_TIME); // Add Weekday // Add date
        }
        //return fromDate != null ? DateUtils.formatDateTime(context, fromDate.toDate().getTime(), DateUtils.FORMAT_SHOW_DATE) : "";
        return dateString;
    }

    public String getLocation() {
        return locations.size() > 0 ? locations.get(0).getName() : "";
    }

    public Location getLocationByClosestDate() {
        // If event is onGoing
        if (this.isOnGoing()) {

            Location firstOngoingLocation = null;

            for (Location location : locations) {
                // Find the ongoing locations
                if (location.isOnGoing()) {
                    if (firstOngoingLocation == null) {
                        firstOngoingLocation = location;
                    } else {
                        // If location started before previous ongoing Location, save it
                        if (location.isOnGoing() && location.getFromDate().isBefore(firstOngoingLocation.getFromDate())) {
                            firstOngoingLocation = location;
                        }
                    }
                }
            }
            return firstOngoingLocation;
            // If event has not yet started:
        } else if (this.getFromDate().isAfterNow()){
            Location closestLocationByDate = null;

            for (Location location : locations) {
                // Default to first location:
                if (closestLocationByDate == null) {
                    closestLocationByDate = location;
                } else if (!location.isOnGoing()){
                    // get earliest fromDate
                    if (location.getFromDate().isBefore(closestLocationByDate.getFromDate())) {
                        closestLocationByDate = location;
                    }
                }
            }

            return closestLocationByDate;
            // Thirdly, if event has finished:
        } else if (this.getToDate().isBeforeNow()){
            Location closestLocationByDate = null;

            for (Location location : locations) {
                // Default to first location:
                if (closestLocationByDate == null) {
                    closestLocationByDate = location;
                } else if (!location.isOnGoing()){
                    // get the latest toDate
                    if (location.getToDate().isAfter(closestLocationByDate.getToDate())) {
                        closestLocationByDate = location;
                    }
                }
            }
            return closestLocationByDate;
        } else {
            // If somehow all chekcs fail (SHOULD NOT HAPPEN), default to returning null
            return null;
        }
    }

    /**
     * Checks if this event is currently on going.
     * @return true if event is ongoing, false otherwise.
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


    public String getAbsoluteImageUrl() {
        String s = "https://" + Constants.API_HOST + "/api" + imageUrl;
        return s;
    }

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
        dest.writeString(this.image);
        dest.writeString(this.imageContentType);
        dest.writeValue(this.defaultPollId);
        dest.writeByte(this.privateEvent ? (byte) 1 : (byte) 0);
        dest.writeString(this.invitationCode);
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
        this.image = in.readString();
        this.imageContentType = in.readString();
        this.defaultPollId = (int) in.readValue(Long.class.getClassLoader());
        this.privateEvent = in.readByte() != 0;
        this.invitationCode = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}

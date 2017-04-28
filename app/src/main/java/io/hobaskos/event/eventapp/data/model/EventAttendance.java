package io.hobaskos.event.eventapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;

/**
 * Created by alex on 3/10/17.
 */
public class EventAttendance implements Parcelable {

    private Long id;

    private DateTime createdDate;

    private EventAttendingType type;

    private Long eventId;

    private String userLogin;

    public EventAttendance(Long eventId, EventAttendingType type) {
        this.eventId = eventId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public EventAttendingType getType() {
        return type;
    }

    public void setType(EventAttendingType type) {
        this.type = type;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "EventAttendance{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", type=" + type +
                ", eventId=" + eventId +
                ", userLogin='" + userLogin + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeSerializable(this.createdDate);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeValue(this.eventId);
        dest.writeString(this.userLogin);
    }

    protected EventAttendance(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.createdDate = (DateTime) in.readSerializable();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : EventAttendingType.values()[tmpType];
        this.eventId = (Long) in.readValue(Long.class.getClassLoader());
        this.userLogin = in.readString();
    }

    public static final Parcelable.Creator<EventAttendance> CREATOR = new Parcelable.Creator<EventAttendance>() {
        @Override
        public EventAttendance createFromParcel(Parcel source) {
            return new EventAttendance(source);
        }

        @Override
        public EventAttendance[] newArray(int size) {
            return new EventAttendance[size];
        }
    };
}

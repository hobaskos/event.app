package io.hobaskos.event.eventapp.data.model;

import org.joda.time.LocalDateTime;

import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;

/**
 * Created by alex on 3/10/17.
 */
public class EventAttendance {

    private Long id;

    private LocalDateTime createdDate;

    private EventAttendingType type;

    private Long eventId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
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
}

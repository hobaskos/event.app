package io.hobaskos.event.eventapp.data.eventbusevent;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by test on 3/13/2017.
 */

public class UpdateEventsEvent {

    private List<Event> events;

    public UpdateEventsEvent(List<Event> events) {
        this.events = events;
    }

    public List<Event> getData() {
        return events;
    }
}

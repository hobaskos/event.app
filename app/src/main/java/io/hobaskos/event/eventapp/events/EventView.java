package io.hobaskos.event.eventapp.events;

import java.util.List;

import io.hobaskos.event.eventapp.models.Event;

/**
 * Created by andre on 1/26/2017.
 */
public interface EventView {
    void showWait();

    void removeWait();

    void onFailure(String errorMessage);

    void setEvents(List<Event> events);
}

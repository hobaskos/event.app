package io.hobaskos.event.eventapp.ui.events;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 1/26/2017.
 */
public interface EventsView {
    void showWait();

    void removeWait();

    void onFailure(String errorMessage);

    void setEvents(List<Event> events);
}

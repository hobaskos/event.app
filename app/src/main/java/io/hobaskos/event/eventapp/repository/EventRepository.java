package io.hobaskos.event.eventapp.repository;

import java.util.List;

import io.hobaskos.event.eventapp.api.EventAPI;
import io.hobaskos.event.eventapp.api.EventService;
import io.hobaskos.event.eventapp.models.Event;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository {

    public List<Event> getEvents() {
        EventAPI api = EventService.createService();
        return api.getEvents();
    }
}

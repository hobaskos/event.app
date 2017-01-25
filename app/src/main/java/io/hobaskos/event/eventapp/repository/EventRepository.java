package io.hobaskos.event.eventapp.repository;

import java.io.IOException;
import java.util.List;

import io.hobaskos.event.eventapp.api.EventAPI;
import io.hobaskos.event.eventapp.api.EventService;
import io.hobaskos.event.eventapp.models.Event;
import retrofit2.Response;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository {

    public Response<List<Event>> getEvents() throws IOException {
        EventAPI api = EventService.createService();
        return api.getEvents().execute();
    }
}

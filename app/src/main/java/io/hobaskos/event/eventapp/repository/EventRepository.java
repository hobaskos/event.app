package io.hobaskos.event.eventapp.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.api.EventApi;
import io.hobaskos.event.eventapp.models.Event;
import rx.Observable;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository implements BaseRepository<Event, Integer> {

    private EventApi eventApi;

    @Inject
    public EventRepository(EventApi eventApi) {
        this.eventApi = eventApi;
    }

    public Observable<List<Event>> getAll() {
        return eventApi.getEvents();
    }

    public Observable<Event> get(Integer id) {
        return eventApi.getEvent(id);
    }
}

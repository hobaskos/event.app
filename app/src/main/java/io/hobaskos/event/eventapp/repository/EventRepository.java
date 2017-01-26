package io.hobaskos.event.eventapp.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.api.EventService;
import io.hobaskos.event.eventapp.models.Event;
import rx.Observable;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository implements BaseRepository<Event, Integer> {

    private EventService eventService;

    @Inject
    public EventRepository(EventService eventService) {
        this.eventService = eventService;
    }

    public Observable<List<Event>> getAll() {
        return eventService.getEvents();
    }

    public Observable<Event> get(Integer id) {
        return eventService.getEvent(id);
    }

    @Override
    public Observable<Event> save(Event item) {
        return eventService.saveEvent(item);
    }

    @Override
    public Observable<Event> update(Event item) {
        return eventService.putEvent(item);
    }

    @Override
    public Observable<Void> delete(Integer id) {
        return eventService.deleteEvent(id);
    }
}

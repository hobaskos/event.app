package io.hobaskos.event.eventapp.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.model.Event;
import rx.Observable;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository implements BaseRepository<Event, Long> {

    private final EventService eventService;

    private final int pageSize = 20;

    @Inject
    public EventRepository(EventService eventService) {
        this.eventService = eventService;
    }

    public Observable<List<Event>> getAll(int page) {
        return eventService.getEvents(page, pageSize);
    }

    public Observable<Event> get(Long id) {
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
    public Observable<Void> delete(Long id) {
        return eventService.deleteEvent(id);
    }
}

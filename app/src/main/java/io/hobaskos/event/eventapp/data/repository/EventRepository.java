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

    private final EventService.Anonymously eventServiceAnonymously;
    private final EventService.Authenticated eventServiceAuthenticated;

    private final int pageSize = 10;

    @Inject
    public EventRepository(EventService.Anonymously eventServiceAnonymously, EventService.Authenticated eventServiceAuthenticated) {
        this.eventServiceAnonymously = eventServiceAnonymously;
        this.eventServiceAuthenticated = eventServiceAuthenticated;
    }

    public Observable<List<Event>> getAll(int page) {
        return eventServiceAnonymously.getEvents(page, pageSize);
    }

    public Observable<Event> get(Long id) {
        return eventServiceAnonymously.getEvent(id);
    }

    @Override
    public Observable<Event> search(int page, String query, double lat, double lon, String distance) {
        return eventServiceAnonymously.search(page, pageSize, query, lat, lon, distance);
    }

    @Override
    public Observable<Event> save(Event item) {
        return eventServiceAuthenticated.saveEvent(item);
    }

    @Override
    public Observable<Event> update(Event item) {
        return eventServiceAuthenticated.putEvent(item);
    }

    @Override
    public Observable<Void> delete(Long id) {
        return eventServiceAuthenticated.deleteEvent(id);
    }
}

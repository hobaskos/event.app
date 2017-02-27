package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import org.joda.time.DateTime;

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

    public static final int PAGE_SIZE = 100;

    @Inject
    public EventRepository(EventService.Anonymously eventServiceAnonymously, EventService.Authenticated eventServiceAuthenticated) {
        this.eventServiceAnonymously = eventServiceAnonymously;
        this.eventServiceAuthenticated = eventServiceAuthenticated;
    }

    public Observable<List<Event>> getAll(int page) {
        return eventServiceAnonymously.getEvents(page, PAGE_SIZE);
    }

    public Observable<Event> get(Long id) {
        return eventServiceAnonymously.getEvent(id);
    }

    @Override
    public Observable<List<Event>> search(int page, double lat, double lon, String distance) {
        //return eventServiceAnonymously.search(page, PAGE_SIZE, lat, lon, distance);

        DateTime fromDate = DateTime.now();
        DateTime toDate = fromDate.plusYears(2);

        Log.i("EventReposiory", "fromDate: " + fromDate + ", toDate: " + toDate);

        return eventServiceAuthenticated
                .search(page, PAGE_SIZE, lat, lon, distance, fromDate, toDate, "fromDate,asc");
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

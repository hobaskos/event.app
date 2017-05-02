package io.hobaskos.event.eventapp.data.repository;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventAttendance;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.model.enumeration.EventAttendingType;
import rx.Observable;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository implements BaseRepository<Event, Long> {

    private final EventService.Anonymously eventServiceAnonymously;
    private final EventService.Authenticated eventServiceAuthenticated;
    private final AccountManager accountManager;

    private final String DEFAULT_SORT = "fromDate,asc";

    public static final int PAGE_SIZE = 20;

    @Inject
    public EventRepository(EventService.Anonymously eventServiceAnonymously,
                           EventService.Authenticated eventServiceAuthenticated,
                           AccountManager accountManager) {
        this.eventServiceAnonymously = eventServiceAnonymously;
        this.eventServiceAuthenticated = eventServiceAuthenticated;
        this.accountManager = accountManager;
    }

    @Override
    public Observable<List<Event>> getAll(int page) {
        return eventServiceAnonymously.getEvents(page, PAGE_SIZE);
    }

    @Override
    public Observable<Event> get(Long id) {
        if (accountManager.isLoggedIn()) {
            return eventServiceAuthenticated.getEvent(id);
        } else {
            return eventServiceAnonymously.getEvent(id);
        }
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

    public Observable<List<Event>> search(int page, String query) {
        if (accountManager.isLoggedIn()) {
            return eventServiceAuthenticated.search(page, PAGE_SIZE, query, DEFAULT_SORT);
        } else {
            return eventServiceAnonymously.search(page, PAGE_SIZE, query, DEFAULT_SORT);
        }
    }

    public Observable<List<Event>> searchNearby(int page, String query, double lat, double lon, String distance,
                                                DateTime fromDate, DateTime toDate, String categories) {
        if (accountManager.isLoggedIn()) {
            return eventServiceAuthenticated
                    .searchNearby(page, PAGE_SIZE, query, lat, lon, distance, fromDate, toDate, categories, DEFAULT_SORT);
        } else {
            return eventServiceAnonymously
                    .searchNearby(page, PAGE_SIZE, query, lat, lon, distance, fromDate, toDate, categories, DEFAULT_SORT);
        }
    }

    public Observable<EventAttendance> attendEvent(Long eventId) {
        return eventServiceAuthenticated.saveAttendance(new EventAttendance(eventId, EventAttendingType.GOING));
    }

    public Observable<Void> leaveEvent(Long eventAttendingId) {
        return eventServiceAuthenticated.deleteAttendance(eventAttendingId);
    }

    public Observable<List<Event>> getAttendingEvents(int page) {
        return eventServiceAuthenticated.getAttendingEvents(page, PAGE_SIZE);
    }

    public Observable<List<Event>> getMyEvents(int page) {
        return eventServiceAuthenticated.getMyEvents(page, PAGE_SIZE, DEFAULT_SORT);
    }

    public Observable<List<User>> getAddendingUsers(Long eventId, int page) {
        return eventServiceAnonymously.getAttendingForEvent(eventId, page, PAGE_SIZE);
    }

    public Observable<Event> getEventByInviteCode(String inviteCode) {
        return eventServiceAuthenticated.getEventByInviteCode(inviteCode);
    }
}

package io.hobaskos.event.eventapp.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.EventCategoryService;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import rx.Observable;

/**
 * Created by alex on 2/15/17.
 */

public class EventCategoryRepository implements BaseRepository<EventCategory, Long> {

    private EventCategoryService eventCategoryService;

    private final static int PAGE_SIZE = 10;

    @Inject
    public EventCategoryRepository(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @Override
    public Observable<List<EventCategory>> getAll(int page) {
        return eventCategoryService.getEventCategories(page, PAGE_SIZE);
    }

    @Override
    public Observable<EventCategory> get(Long id) {
        return eventCategoryService.getEventCategory(id);
    }

    @Override
    public Observable<List<EventCategory>> search(int page, double lat, double lon, String distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<EventCategory> save(EventCategory eventCategory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<EventCategory> update(EventCategory eventCategory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Void> delete(Long id) {
        throw new UnsupportedOperationException();
    }
}

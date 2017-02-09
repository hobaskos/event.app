package io.hobaskos.event.eventapp.data.repository;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Created by alex on 1/26/17.
 */

public interface BaseRepository<T, I> {

    Observable<List<T>> getAll(int page);
    Observable<T> get(I id);
    Observable<T> search(int page, String query, double lat, double lon, String distance);
    Observable<T> save(T item);
    Observable<T> update(T item);
    Observable<Void> delete(I id);
}

package io.hobaskos.event.eventapp.repository;

import java.util.List;

import rx.Observable;

/**
 * Created by alex on 1/26/17.
 */

public interface BaseRepository<T, I> {

    Observable<List<T>> getAll();
    Observable<T> get(I id);
    Observable<T> save(T item);
    Observable<T> update(T item);
    Observable<Void> delete(I id);
}

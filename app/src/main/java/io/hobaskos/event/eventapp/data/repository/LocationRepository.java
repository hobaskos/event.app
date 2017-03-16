package io.hobaskos.event.eventapp.data.repository;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.LocationService;
import io.hobaskos.event.eventapp.data.model.Location;
import rx.Observable;

/**
 * Created by hansp on 13.03.2017.
 */

public class LocationRepository {

    private final LocationService service;

    @Inject
    public LocationRepository(LocationService service) {
        this.service = service;
    }

    public Observable<Location> save(Location location) {
        return service.save(location);
    }

    public Observable<Location> put(Location location) { return service.put(location); }

    public Observable<Void> remove(Location location) { return service.remove(location.getId());}
}

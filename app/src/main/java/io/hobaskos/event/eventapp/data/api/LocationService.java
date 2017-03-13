package io.hobaskos.event.eventapp.data.api;

import io.hobaskos.event.eventapp.data.model.Location;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by hansp on 13.03.2017.
 */

public interface LocationService {

    @POST("api/locations")
    Observable<Location> save(@Body Location location);
}

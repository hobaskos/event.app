package io.hobaskos.event.eventapp.data.api;

import io.hobaskos.event.eventapp.data.model.Location;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by hansp on 13.03.2017.
 */

public interface LocationService {

    @POST("api/locations")
    Observable<Location> save(@Body Location location);

    @PUT("api/locations")
    Observable<Location> put(@Body Location location);
}

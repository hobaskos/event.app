package io.hobaskos.event.eventapp.data.api;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Location;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hansp on 13.03.2017.
 */

public interface LocationService {

    @POST("api/locations")
    Observable<Location> save(@Body Location location);

    @PUT("api/locations")
    Observable<Location> put(@Body Location location);

    @DELETE("api/locations/{id}")
    Observable<Void> remove(@Path("id") Long id);

    @GET("api/events/{id}/locations")
    Observable<List<Location>> getLocationsForEvent(@Path("id") Long id,
                                                    @Query("page") int page,
                                                    @Query("size") int pageSize);
}

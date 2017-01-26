package io.hobaskos.event.eventapp.api;

import java.util.List;

import io.hobaskos.event.eventapp.models.Event;
import rx.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by andre on 1/25/2017.
 */
public interface EventService
{
    @GET("api/events")
    Observable<List<Event>> getEvents();

    @GET("api/events/{id}")
    Observable<Event> getEvent(@Path("id") int id);

    @POST("api/events")
    Observable<Event> saveEvent(@Body Event event);

    @PUT("api/events")
    Observable<Event> putEvent(@Body Event event);

    @DELETE("api/events/{id}")
    Observable<Void> deleteEvent(@Path("id") int id);
}

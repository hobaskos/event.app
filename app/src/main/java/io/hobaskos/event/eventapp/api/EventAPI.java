package io.hobaskos.event.eventapp.api;

import java.util.List;

import io.hobaskos.event.eventapp.models.Event;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by andre on 1/25/2017.
 */
public interface EventAPI {

    @GET("api/events")
    List<Event> getEvents();

    @GET("api/events/{id}")
    Event getEvent(@Path("id") int id);

    @POST("api/events")
    Event postEvent(@Body Event event);

    @PUT("api/events")
    Event putEvent(@Body Event event);

    @DELETE("api/events/{id}")
    void deleteEvent(@Path("id") int id);
}

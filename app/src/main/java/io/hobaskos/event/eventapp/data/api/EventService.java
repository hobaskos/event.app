package io.hobaskos.event.eventapp.data.api;

import org.joda.time.DateTime;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventAttendance;
import io.hobaskos.event.eventapp.data.model.User;
import retrofit2.http.Query;
import rx.Observable;
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
    interface Anonymously {

        @GET("api/events")
        Observable<List<Event>> getEvents(@Query("page") int page,
                                          @Query("size") int pageSize);

        @GET("api/events/{id}")
        Observable<Event> getEvent(@Path("id") Long id);

        @GET("api/_search/events-nearby")
        Observable<List<Event>> searchNearby(@Query("page") int page,
                                             @Query("size") int pageSize,
                                             @Query("query") String query,
                                             @Query("lat") double lat,
                                             @Query("lon") double lon,
                                             @Query("distance") String distance,
                                             @Query("fromDate") DateTime fromDate,
                                             @Query("toDate") DateTime toDate,
                                             @Query("categories") String categories,
                                             @Query("sort") String sort);

        @GET("api/_search/events")
        Observable<List<Event>> search(@Query("page") int page,
                                       @Query("size") int pageSize,
                                       @Query("query") String query,
                                       @Query("sort") String sort);

        @GET("api/events/{id}/attending")
        Observable<List<User>> getAttendingForEvent(@Path("id") Long eventId,
                                                    @Query("page") int page,
                                                    @Query("size") int pageSize);
    }

    interface Authenticated {
        @GET("api/events/{id}")
        Observable<Event> getEvent(@Path("id") Long id);

        @POST("api/events")
        Observable<Event> saveEvent(@Body Event event);

        @PUT("api/events")
        Observable<Event> putEvent(@Body Event event);

        @DELETE("api/events/{id}")
        Observable<Void> deleteEvent(@Path("id") Long id);

        @GET("api/_search/events")
        Observable<List<Event>> search(@Query("page") int page,
                                       @Query("size") int pageSize,
                                       @Query("query") String query,
                                       @Query("sort") String sort);

        @GET("api/_search/events-nearby")
        Observable<List<Event>> searchNearby(@Query("page") int page,
                                       @Query("size") int pageSize,
                                       @Query("query") String query,
                                       @Query("lat") double lat,
                                       @Query("lon") double lon,
                                       @Query("distance") String distance,
                                       @Query("fromDate") DateTime fromDate,
                                       @Query("toDate") DateTime toDate,
                                       @Query("categories") String categories,
                                       @Query("sort") String sort);

        @POST("api/event-user-attendings")
        Observable<EventAttendance> saveAttendance(@Body EventAttendance attendance);

        @DELETE("api/event-user-attendings/{id}")
        Observable<Void> deleteAttendance(@Path("id") Long id);

        @GET("api/account/attending-events?sort=fromDate,asc")
        Observable<List<Event>> getAttendingEvents(@Query("page") int page,
                                                   @Query("size") int pageSize);

        @GET("/api/account/my-events")
        Observable<List<Event>> getMyEvents(@Query("page") int page,
                                            @Query("size") int pageSize,
                                            @Query("sort") String sort);

        @GET("api/event-by-invite/{code}")
        Observable<Event> getEventByInviteCode(@Path("code") String code);
    }
}

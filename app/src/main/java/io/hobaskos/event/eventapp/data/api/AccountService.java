package io.hobaskos.event.eventapp.data.api;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import retrofit2.http.Body;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public interface AccountService {

    @GET("api/authenticate")
    Observable<String> login(@Body JwtToken token);

    @GET("/api/account/attending-events")
    Observable<List<Event>> getAttendingEvents();

}

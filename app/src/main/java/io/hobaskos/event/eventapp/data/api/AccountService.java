package io.hobaskos.event.eventapp.data.api;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public interface AccountService {

    @GET("api/authenticate")
    Observable<String> login(@Body JwtToken token);
}

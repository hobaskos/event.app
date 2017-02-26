package io.hobaskos.event.eventapp.data.api;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public interface UserService {

    @POST("api/authenticate")
    Observable<JwtToken> login(@Body LoginVM user);

    @GET("api/account")
    Observable<User> getAccount();
}

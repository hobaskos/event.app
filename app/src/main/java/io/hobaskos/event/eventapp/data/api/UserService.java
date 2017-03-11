package io.hobaskos.event.eventapp.data.api;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialType;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public interface UserService {

    @POST("api/authenticate")
    Observable<JwtToken> login(@Body LoginVM user);

    @POST("api/authenticate/social")
    Observable<JwtToken> login(@Body SocialUserVM userVM);

    @GET("api/account")
    Observable<User> getAccount();

    @GET("api/events/{id}/attending")
    Observable<List<User>> getAttendingForEvent(@Path("id") Long id);
}

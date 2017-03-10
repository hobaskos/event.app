package io.hobaskos.event.eventapp.data.api;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialType;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @POST("api/account")
    Observable<Void> saveAccount(@Body User user);
}

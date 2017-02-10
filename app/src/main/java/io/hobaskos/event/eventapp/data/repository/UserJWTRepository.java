package io.hobaskos.event.eventapp.data.repository;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserJWTService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import io.hobaskos.event.eventapp.data.service.JwtTokenProxy;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class UserJWTRepository {

    private final UserJWTService userJWTService;
    private final JwtTokenProxy jwtTokenProxy;

    @Inject
    public UserJWTRepository(UserJWTService userJWTService, JwtTokenProxy jwtTokenProxy)
    {
        this.userJWTService = userJWTService;
        this.jwtTokenProxy = jwtTokenProxy;
    }

    public boolean login(UserLogin userLogin)
    {
        Observable<JwtToken> token = userJWTService.login(userLogin);

        // Todo: handle 401 ??

        token.subscribe(new Action1<JwtToken>() {
            @Override
            public void call(JwtToken jwtToken) {
                jwtTokenProxy.put(jwtToken.getIdToken());
            }
        });

        return true;
    }

}

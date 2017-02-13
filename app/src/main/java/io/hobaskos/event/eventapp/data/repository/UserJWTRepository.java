package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.AccountService;
import io.hobaskos.event.eventapp.data.api.UserJWTService;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import io.hobaskos.event.eventapp.data.service.JwtTokenProxy;
import io.hobaskos.event.eventapp.ui.login.LoginPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    public void login(UserLogin userLogin, LoginPresenter loginPresenter)
    {
        Observable<JwtToken> tokenService = userJWTService.login(userLogin);

        tokenService
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    jwtTokenProxy.put(token.getIdToken());
                    loginPresenter.callbackOnSuccess();
            }, loginPresenter::callbackOnError
        );
    }

}

package io.hobaskos.event.eventapp.data.repository;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserJWTService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import io.hobaskos.event.eventapp.data.service.JwtStorageProxy;
import io.hobaskos.event.eventapp.ui.login.LoginPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JwtRepository {

    private final UserJWTService userJWTService;
    private final JwtStorageProxy jwtStorageProxy;


    @Inject
    public JwtRepository(UserJWTService userJWTService, JwtStorageProxy jwtStorageProxy)
    {
        this.userJWTService = userJWTService;
        this.jwtStorageProxy = jwtStorageProxy;
    }

    public void login(UserLogin userLogin, LoginPresenter loginPresenter)
    {
        Observable<JwtToken> tokenService = userJWTService.login(userLogin);

        tokenService
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    jwtStorageProxy.put(token.getIdToken());
                    loginPresenter.callbackOnSuccess();
            }, loginPresenter::callbackOnError
        );
    }

}

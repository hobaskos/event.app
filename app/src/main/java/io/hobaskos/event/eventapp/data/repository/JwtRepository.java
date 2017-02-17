package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserJWTService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
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

    public Observable<Void> login(LoginVM loginVM, LoginPresenter loginPresenter)
    {
        Log.i("JwtRepository", "JwtRepository->login()");
        Observable<JwtToken> tokenService = userJWTService.login(loginVM);

        return tokenService
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(token -> {
            jwtStorageProxy.put(token.getIdToken());
            return null;
        });
    }

}

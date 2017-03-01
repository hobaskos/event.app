package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import com.facebook.AccessToken;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserRepository {
    private final String TAG = "UserRepository";
    private final UserService service;
    private final JwtStorageProxy localStorage;

    @Inject
    public UserRepository(UserService service, JwtStorageProxy localStorage)
    {
        this.service = service;
        this.localStorage = localStorage;
    }

    public Observable<Void> login(LoginVM loginVM)
    {
        Observable<JwtToken> token = service.login(loginVM);

        return token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(t -> {
                    localStorage.put(t.getIdToken());
                    return null;
                });
    }

    public Observable<Void> login(SocialUserVM socialUserVM)
    {
        Log.i(TAG, socialUserVM.getAccessToken());
        Observable<JwtToken> token = service.login(socialUserVM);

        return token.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(t -> {
            localStorage.put(t.getIdToken());
            Log.i(TAG, t.getIdToken());
            return null;
        });
    }
}

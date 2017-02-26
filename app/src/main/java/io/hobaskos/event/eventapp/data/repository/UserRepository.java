package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import com.facebook.AccessToken;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserRepository {

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

    public boolean login(SocialUserVM socialUserVM)
    {
        // Todo: Do some business logic
        // Todo: save to server

        return AccessToken.getCurrentAccessToken() != null;
    }

    public Observable<User> getAccount()
    {
        if(localStorage.isSet())
        {
            Log.i("Token", "Token is set");
            Log.i("Token", localStorage.get());
        }

        return service.getAccount();
    }
}

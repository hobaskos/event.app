package io.hobaskos.event.eventapp;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserManager {

    private JwtStorageProxy localStorage;
    private UserRepository repository;

    @Inject
    public UserManager(JwtStorageProxy localStorage, UserRepository repository)
    {
        this.localStorage = localStorage;
        this.repository = repository;
    }

    public boolean isLoggedIn()
    {
        return localStorage.isSet() || (AccessToken.getCurrentAccessToken() != null);
    }

    public void logout()
    {
        localStorage.remove();
        LoginManager.getInstance().logOut();
    }

    public Observable<User> getAccount()
    {
        return repository.getAccount();
    }

}

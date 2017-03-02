package io.hobaskos.event.eventapp;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.parceler.Repository;

import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;

/**
 * Created by hansp on 25.02.2017.
 */

public class AccountManager {

    private JwtStorageProxy jwtStorageProxy;
    private UserRepository userRepository;

    public AccountManager(JwtStorageProxy jwtStorageProxy, UserRepository userRepository)
    {
        this.jwtStorageProxy = jwtStorageProxy;
        this.userRepository = userRepository;
    }

    public boolean isLoggedIn()
    {
        return jwtStorageProxy.isSet() || (AccessToken.getCurrentAccessToken() != null);
    }

    public void logout()
    {
        jwtStorageProxy.remove();
        LoginManager.getInstance().logOut();
    }

    public Observable<User> getAccount()
    {
        return userRepository.getAccount();
    }

}

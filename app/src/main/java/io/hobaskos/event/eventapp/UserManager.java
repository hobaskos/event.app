package io.hobaskos.event.eventapp;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserManager {

    private JwtStorageProxy jwtStorageProxy;

    public UserManager(JwtStorageProxy jwtStorageProxy)
    {
        this.jwtStorageProxy = jwtStorageProxy;
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

}

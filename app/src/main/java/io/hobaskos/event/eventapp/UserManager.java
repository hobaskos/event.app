package io.hobaskos.event.eventapp;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.FBAccessTokenStorageProxy;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserManager {

    private JwtStorageProxy jwtStorageProxy;
    private FBAccessTokenStorageProxy fbAccessTokenStorageProxy;

    public UserManager(JwtStorageProxy jwtStorageProxy, FBAccessTokenStorageProxy fbAccessTokenStorageProxy)
    {
        this.jwtStorageProxy = jwtStorageProxy;
        this.fbAccessTokenStorageProxy = fbAccessTokenStorageProxy;
    }

    public boolean isLoggedIn()
    {
        return jwtStorageProxy.isSet() || fbAccessTokenStorageProxy.isSet();
    }

    public void logout()
    {
        jwtStorageProxy.remove();
        fbAccessTokenStorageProxy.remove();
    }

}

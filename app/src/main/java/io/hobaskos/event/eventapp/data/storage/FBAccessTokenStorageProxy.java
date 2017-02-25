package io.hobaskos.event.eventapp.data.storage;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.AccessToken;

/**
 * Created by hansp on 25.02.2017.
 */

public class FBAccessTokenStorageProxy {

    private final PersistentStorage storage;
    private final String ACCESS_TOKEN_KEY = "access_token";
    private final String USER_ID_KEY = "user_id";

    @Inject
    public FBAccessTokenStorageProxy(PersistentStorage storage)
    {
        this.storage = storage;
    }

    public AccessToken get()
    {
        return new AccessToken(storage.get(USER_ID_KEY), storage.get(ACCESS_TOKEN_KEY));
    }

    public boolean isSet()
    {
        return storage.isSet(ACCESS_TOKEN_KEY) && storage.isSet(USER_ID_KEY);
    }

    public boolean isValid()
    {
        return false;
    }

    public boolean put(AccessToken accessToken) {
        boolean ok = storage.put(USER_ID_KEY, accessToken.getUserId());
        return ok && storage.put(ACCESS_TOKEN_KEY, accessToken.getAccessToken());

    }

    public void remove()
    {
        if(isSet())
        {
            storage.remove(USER_ID_KEY);
            storage.remove(ACCESS_TOKEN_KEY);
        }
    }

}

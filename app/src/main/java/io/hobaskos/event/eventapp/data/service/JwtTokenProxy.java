package io.hobaskos.event.eventapp.data.service;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.PersistentStorage;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JwtTokenProxy {

    private final PersistentStorage persistentStorage;
    private final String KEY = "jwt_token";

    @Inject
    public JwtTokenProxy(PersistentStorage persistentStorage)
    {
        this.persistentStorage = persistentStorage;
    }

    public String get()
    {
        return persistentStorage.get(KEY);
    }

    public boolean isSet()
    {
        return persistentStorage.isSet(KEY);
    }

    public boolean isValid()
    {
        return false;
    }

    public boolean put(String token)
    {
        return persistentStorage.put(KEY, token);
    }

}

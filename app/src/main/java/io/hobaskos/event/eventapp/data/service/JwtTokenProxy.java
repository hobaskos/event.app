package io.hobaskos.event.eventapp.data.service;

import android.content.SharedPreferences;

import io.hobaskos.event.eventapp.data.PersistentStorage;
import io.hobaskos.event.eventapp.data.api.EventService;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JwtTokenProxy {

    public PersistentStorage persistentStorage;
    private final String KEY = "jwt_token";

    public JwtTokenProxy()
    {

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

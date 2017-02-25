package io.hobaskos.event.eventapp.data.storage;

import javax.inject.Inject;

/**
 * Created by osvold.hans.petter on 10.02.2017.
 */

public class JwtStorageProxy {

    private final PersistentStorage persistentStorage;
    private final String KEY = "jwt_token";

    @Inject
    public JwtStorageProxy(PersistentStorage persistentStorage)
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

    public void remove()
    {
        if(isSet())
        {
            persistentStorage.remove(KEY);
        }
    }

}

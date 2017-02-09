package io.hobaskos.event.eventapp.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;

import dagger.Module;
import io.hobaskos.event.eventapp.App;

/**
 * Created by osvold.hans.petter on 09.02.2017.
 */

@Singleton
@Module
public class LocalStorage {

    private App app;
    private String PREFS_NAME = "local_storage";
    private String key;
    private String defaultValue;

    @Inject
    public LocalStorage(@NonNull App app, @NonNull String key, @NonNull String defaultValue)
    {
        this.app = app;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String get()
    {
        SharedPreferences preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, 0);

        return preferences.getString(key, defaultValue);
    }

    public void put(String value)
    {
        SharedPreferences preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, 0);

        if(preferences.contains(key))
        {
            // Todo: apply vs commit
            preferences.edit()
                       .putString(key, value)
                       .apply();
        }
    }

    public boolean isSet()
    {
        return app.getBaseContext()
                .getSharedPreferences(PREFS_NAME, 0)
                .contains(key);
    }

}

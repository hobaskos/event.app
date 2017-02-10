package io.hobaskos.event.eventapp.data;

import android.content.SharedPreferences;

import io.hobaskos.event.eventapp.App;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by osvold.hans.petter on 09.02.2017.
 */

public class PersistentStorage {

    private String PREFS_NAME = "ls";
    private int MODE = MODE_PRIVATE;
    private SharedPreferences preferences;

    public PersistentStorage(App app)
    {
        preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, MODE);
    }

    public String get(String key)
    {
        return preferences.getString(key, "");
    }

    public boolean put(String key, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean isSet(String key)
    {
        return preferences.contains(key);
    }
}


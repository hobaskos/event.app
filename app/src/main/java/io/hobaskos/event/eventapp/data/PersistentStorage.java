package io.hobaskos.event.eventapp.data;

import android.content.SharedPreferences;

import io.hobaskos.event.eventapp.App;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by osvold.hans.petter on 09.02.2017.
 */

public class PersistentStorage {

    private App app;
    private String PREFS_NAME = "ls";
    private int MODE = MODE_PRIVATE;
    private SharedPreferences preferences;

    public PersistentStorage(App app)
    {
        this.app = app;
        preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, MODE);
    }

    public String get(String key)
    {
        return preferences.getString(key, "");
    }

    public String getJWTToken()
    {
        return get("id_token");
    }

    public boolean put(String key, String value)
    {
        preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, MODE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean putJWTToken(String value)
    {
        return put("id_token", value);
    }
}


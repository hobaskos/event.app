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

    public PersistentStorage(App app) {
        preferences = app.getBaseContext().getSharedPreferences(PREFS_NAME, MODE);
    }

    public String get(String key)
    {
        return preferences.getString(key, "");
    }

    public boolean put(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
        return true;
    }

    public boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    // http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
    public boolean putDouble(String key, double value) {
        Long longValue = Double.doubleToLongBits(value);
        return preferences.edit()
                            .putLong(key, longValue)
                            .commit();
    }

    public double getDouble(String key, double defaultValue) {
        Long value = preferences.getLong(key, Double.doubleToRawLongBits(defaultValue));
        return Double.longBitsToDouble(value);
    }


    public boolean isSet(String key)
    {
        return preferences.contains(key);
    }

    public void remove(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }
}


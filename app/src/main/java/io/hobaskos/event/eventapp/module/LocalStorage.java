package io.hobaskos.event.eventapp.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;
import android.content.SharedPreferences.Editor;

import dagger.Module;

/**
 * Created by osvold.hans.petter on 09.02.2017.
 */

@Singleton
@Module
public class LocalStorage {

    @Inject
    private Context context;
    @Inject
    private String PREFS_NAME;

    public String get(String key)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        return settings.getString(key, "");
    }

    public void put(String key, String value)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        if(settings.contains(key))
        {
            Editor editor = settings.edit();
            editor.putString(key, value);
            // Todo: apply vs commit
            editor.commit();
        }
    }

}

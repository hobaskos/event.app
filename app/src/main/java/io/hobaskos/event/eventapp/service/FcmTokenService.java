package io.hobaskos.event.eventapp.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;

/**
 * Created by alex on 3/26/17.
 */

public class FcmTokenService extends FirebaseInstanceIdService {

    public final static String TAG = FcmTokenService.class.getName();
    public final static String FIREBASE_TOKEN_KEY = "FireBase_Token_Key";

    private SharedPreferences preferences;

    public FcmTokenService() {
        super();
        preferences = App.getInst().getSharedPreferences(PersistentStorage.PREFS_NAME,
                PersistentStorage.MODE);
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        put(refreshedToken);
    }

    public boolean put(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIREBASE_TOKEN_KEY, token);
        editor.apply();
        return true;
    }
}

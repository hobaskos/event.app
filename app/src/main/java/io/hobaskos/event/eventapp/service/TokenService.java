package io.hobaskos.event.eventapp.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.storage.PersistentStorage;

/**
 * Created by alex on 3/26/17.
 */

public class TokenService extends FirebaseInstanceIdService {

    public final static String TAG = TokenService.class.getName();
    public final static String FIREBASE_TOKEN_KEY = "FireBase_Token_Key";

    private PersistentStorage persistentStorage;

    @Inject
    public TokenService(PersistentStorage persistentStorage) {
        this.persistentStorage = persistentStorage;
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        persistentStorage.put(FIREBASE_TOKEN_KEY, refreshedToken);
    }
}

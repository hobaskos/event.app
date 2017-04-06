package io.hobaskos.event.eventapp.data.repository;

import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.eventbus.UserHasLoggedInEvent;
import io.hobaskos.event.eventapp.data.model.Device;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.service.FcmTokenService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserRepository {

    public final static String TAG = UserRepository.class.getName();

    private final static String LOCAL_ACCOUNT_KEY = UserRepository.class.getName() + "LOCAL_ACCOUNT_KEY";

    private final UserService service;
    private final JwtStorageProxy jwtStorage;
    private final PersistentStorage persistentStorage;

    /**
     *
     * @param service UserService which handles communication with the backend.
     * @param jwtStorage Proxy for handling persistent storage of Jwt tokens.
     */
    @Inject
    public UserRepository(UserService service, JwtStorageProxy jwtStorage, PersistentStorage persistentStorage) {
        this.service = service;
        this.jwtStorage = jwtStorage;
        this.persistentStorage = persistentStorage;
    }

    /**
     *
     * @param loginVM View Model for a regular (username/password) login.
     * @return
     */
    public Observable<Void> login(LoginVM loginVM) {
        Observable<JwtToken> token = service.login(loginVM);

        Log.d(TAG, "login: starting login routine");
        return token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(t -> {
                    Log.d(TAG, "login: mapping callback to save data");
                    jwtStorage.put(t.getIdToken());
                    sendDeviceTokenToServer();
                    service.getAccount()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::setLocalAccount, throwable -> {
                                Log.i("UserRepository", throwable.getMessage());
                            });
                    return null;
                });
    }

    /**
     *
     * @param socialUserVM View Model for a social (Facebook, etc) Login.
     * @return
     */
    public Observable<Void> login(SocialUserVM socialUserVM) {
        Observable<JwtToken> token = service.login(socialUserVM);

        Log.d(TAG, "login: starting login routine");
        return token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(t -> {
                    Log.d(TAG, "login: mapping callback to save data");
                    jwtStorage.put(t.getIdToken());
                    sendDeviceTokenToServer();
                    service.getAccount()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::setLocalAccount, throwable -> {
                                Log.i("UserRepository", throwable.getMessage());
                    });
                    return null;
                });
    }

    /**
     *
     * @return the current User-object of the logged-in Account.
     */
    public Observable<User> getAccount() {
        Observable<User> account = service.getAccount();

        return account;
    }

    public Observable<Void> saveAccount(User user) {
        return service.saveAccount(user);
    }

    public User getLocalAccount() {
        String serialized = persistentStorage.get(LOCAL_ACCOUNT_KEY);
        return new Gson().fromJson(serialized, User.class);
    }

    public void setLocalAccount(User user) {
        Log.i("UserRepository", "User=" + user.toString());
        String serialized = new Gson().toJson(user);
        persistentStorage.put(LOCAL_ACCOUNT_KEY, serialized);
        EventBus.getDefault().postSticky(new UserHasLoggedInEvent(user.getName(), user.getProfileImageUrl()));
    }

    public void removeLocalAccount() {
        persistentStorage.remove(LOCAL_ACCOUNT_KEY);
    }

    /**
     * Get attending users for event
     * @param eventId
     * @return list of users
     */
    public Observable<List<User>> getAttendingForEvent(Long eventId) {
        return service.getAttendingForEvent(eventId);
    }

    private void sendDeviceTokenToServer() {
        String token = persistentStorage.get(FcmTokenService.FIREBASE_TOKEN_KEY);

        if (token == null || token.equals("")) {
            Log.d(TAG, "sendDeviceTokenToServer: token was not found");
            return;
        }

        Log.d(TAG, "sendDeviceTokenToServer: " + token);
        service.saveDevice(new Device(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        device -> {},
                        throwable -> Log.d(TAG, throwable.getMessage())
                );
    }
}

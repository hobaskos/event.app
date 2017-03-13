package io.hobaskos.event.eventapp.data.repository;

import com.facebook.AccessToken;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.VoidViewState;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.model.JwtToken;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class UserRepository {

    private final UserService service;
    private final JwtStorageProxy localStorage;

    /**
     *
     * @param service UserService which handles communication with the backend.
     * @param localStorage Proxy for handling persistent storage of Jwt tokens.
     */
    @Inject
    public UserRepository(UserService service, JwtStorageProxy localStorage) {
        this.service = service;
        this.localStorage = localStorage;
    }

    /**
     *
     * @param loginVM View Model for a regular (username/password) login.
     * @return
     */
    public Observable<Void> login(LoginVM loginVM) {
        Observable<JwtToken> token = service.login(loginVM);

        return token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(t -> {
                    localStorage.put(t.getIdToken());
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

        return token
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(t -> {
                    localStorage.put(t.getIdToken());
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

    /**
     * Get attending users for event
     * @param eventId
     * @return list of users
     */
    public Observable<List<User>> getAttendingForEvent(Long eventId) {
        return service.getAttendingForEvent(eventId);
    }
}

package io.hobaskos.event.eventapp.ui.login;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import io.hobaskos.event.eventapp.data.repository.UserJWTRepository;
import io.hobaskos.event.eventapp.ui.base.BaseMvpPresenter;
import rx.Observable;
import rx.Observer;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginPresenter implements BaseMvpPresenter<UserLogin> {

    private UserJWTRepository repository;
    private Observable<UserLogin> userLoginObservable = Observable.empty();

    @Inject
    public LoginPresenter(UserJWTRepository repository) {
        this.repository = repository;
    }

    @Override
    public void subscribe(Observer<UserLogin> observer) {
        userLoginObservable.subscribe(observer);
    }

    public Observable<UserLogin> getObservable()
    {
        return userLoginObservable;
    }
}

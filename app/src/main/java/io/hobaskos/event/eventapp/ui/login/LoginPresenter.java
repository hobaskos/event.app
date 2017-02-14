package io.hobaskos.event.eventapp.ui.login;

import android.util.Log;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.ui.base.BaseMvpPresenter;
import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginPresenter implements BaseMvpPresenter<Notification<Response>> {

    private JwtRepository repository;
    private PublishSubject<Notification<Response>> userLoginObservable = PublishSubject.create();

    @Inject
    public LoginPresenter(JwtRepository repository) {
        this.repository = repository;
    }

    public void login(String login, String password, boolean rememberMe)
    {
        LoginVM loginVM = new LoginVM(login, password, rememberMe);
        repository.login(loginVM, this);
    }

    public void callbackOnSuccess()
    {
        Log.i("loginPresenter", "callback success");

        Notification<Response> notification = Notification.createOnNext(new Response(true, "Success"));

        userLoginObservable.onNext(notification);


    }

    public void callbackOnError(Throwable throwable)
    {
        Log.i("loginPresenter", "callbackError(" + throwable.getMessage() + ")");
        //userLoginObservable.onError(throwable);
        Notification<Response> notification = Notification.createOnNext(new Response(false, throwable.getMessage()));
        userLoginObservable.onNext(notification);
    }

    @Override
    public void subscribe(Observer<Notification<Response>> observer) {
        userLoginObservable.subscribe(observer);
    }

    public Observable<Notification<Response>> getObservable()
    {
        return userLoginObservable;
    }
}

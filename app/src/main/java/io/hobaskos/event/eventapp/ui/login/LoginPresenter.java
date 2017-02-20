package io.hobaskos.event.eventapp.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.data.service.JwtStorageProxy;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private JwtRepository repository;
    private JwtStorageProxy storageProxy;

    @Inject
    public LoginPresenter(JwtRepository repository) {
        this.repository = repository;
    }

    // Username/Password-login
    public void login(String login, String password, boolean rememberMe)
    {
        LoginVM loginVM = new LoginVM(login, password, rememberMe);

        repository.login(loginVM, this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached())
                {
                    getView().showError(new Response(false, e.getMessage()));
                }
            }

            @Override
            public void onNext(Void aBoolean) {
                if(isViewAttached())
                {
                    getView().showSuccess(new Response(true, "Success"));
                }
            }
        });


    }
}

package io.hobaskos.event.eventapp.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private JwtRepository jwtRepository;
    private UserRepository userRepository;

    @Inject
    public LoginPresenter(JwtRepository jwtRepository, UserRepository userRepository) {
        this.jwtRepository = jwtRepository;
        this.userRepository = userRepository;
    }

    // Username/Password-login
    public void login(LoginVM loginVM)
    {
        Log.i("LoginPresenter", "Inside login(LoginVM loginVM)");

        jwtRepository.login(loginVM)
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
                            getView().showSuccess(new Response(true, ""));
                        }
                    }
                });
    }

    public void login(SocialUserVM socialLoginVM)
    {
        // Todo: login user at server

        if(userRepository.login(socialLoginVM))
        {
            getView().showSuccess(new Response(true, ""));
        }
        else
        {
            getView().showError(new Response(false, "Error"));
        }

    }

}
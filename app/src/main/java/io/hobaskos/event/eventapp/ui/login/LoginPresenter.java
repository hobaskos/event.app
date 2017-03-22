package io.hobaskos.event.eventapp.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    public UserRepository userRepository;

    @Inject
    public LoginPresenter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Username/Password-login
    public void login(LoginVM loginVM) {
        Log.i("LoginPresenter", "Inside login(LoginVM loginVM)");

        userRepository.login(loginVM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached() && getView() != null) {
                            getView().hasNotLoggedInSuccessfully();
                        }
                    }

                    @Override
                    public void onNext(Void aBoolean) {
                        if(isViewAttached() && getView() != null) {
                            getView().hasLoggedInSuccessfully();
                        }
                    }
                });
    }

    public void login(SocialUserVM socialLoginVM) {
        userRepository.login(socialLoginVM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached() && getView() != null) {
                            getView().hasNotLoggedInSuccessfully();
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        if(isViewAttached() && getView() != null) {
                            getView().hasLoggedInSuccessfully();
                        }
                    }
                });
    }

}
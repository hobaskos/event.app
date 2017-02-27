package io.hobaskos.event.eventapp.ui.main;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.UserManager;
import io.hobaskos.event.eventapp.data.model.User;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private UserManager userManager;

    @Inject
    public MainPresenter(UserManager userManager)
    {
        this.userManager = userManager;
    }

    public void fetchAccountInfo()
    {

        Log.i("MainPresenter", "fetchAccountInfo()");

        userManager.getAccount().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // I/MainPresenter: HTTP 401 Unauthorized
                Log.i("MainPresenter", e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(User user) {
                Log.i("MainPresenter", "User fetched successfully.");
                Log.i("MainPresenter", "Name of user: " + user.getFirstName() + " " + user.getLastName());
                Log.i("User", user.toString());
                if(isViewAttached())
                {
                    if(user.getProfileImageUrl() != null)
                    {
                        getView().setNavigationHeaderImage(user.getProfileImageUrl());
                    } else {
                        getView().setNavigationHeaderImage("");
                    }

                    getView().setNavigationHeaderText(user.getFirstName() + " " + user.getLastName());
                }
            }
        });
    }

    public void logout()
    {
        userManager.logout();
        getView().hideNavigationHeader();
    }

    public void onCreateOptionsMenu()
    {
        if(isViewAttached())
        {
            if(userManager.isLoggedIn())
            {
                fetchAccountInfo();
                getView().viewAuthenticatedNavigation();
            }
            else {
                getView().viewAnonymousNavigation();
            }
        }
    }



}

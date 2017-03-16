package io.hobaskos.event.eventapp.ui.main;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 25.02.2017.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private AccountManager accountManager;
    private EventRepository eventRepository;

    @Inject
    public MainPresenter(AccountManager accountManager, EventRepository eventRepository) {
        this.accountManager = accountManager;
        this.eventRepository = eventRepository;
    }

    public void fetchAccountInfo()
    {

        Log.i("MainPresenter", "fetchAccountInfo()");

        accountManager.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
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
                            getView().setNavigationHeaderText(user.getFirstName() + " " + user.getLastName());
                        }
                    }
                });
    }

    public void logout()
    {
        accountManager.logout();
        getView().hideNavigationHeader();
    }

    public void onLoginState()
    {
        if(isViewAttached())
        {
            if(accountManager.isLoggedIn())
            {
                fetchAccountInfo();
                getView().viewAuthenticatedNavigation();
            }
            else {
                getView().viewAnonymousNavigation();
            }
        }
    }

    public void getEventFromInviteCode(String inviteCode, Observer<Event> callback) {
        eventRepository.getEventByInviteCode(inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }
}
package io.hobaskos.event.eventapp.ui.main;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.eventbus.UserHasLoggedInEvent;
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

    private static final String TAG = MainPresenter.class.getName();

    private AccountManager accountManager;
    private EventRepository eventRepository;

    @Inject
    public MainPresenter(AccountManager accountManager, EventRepository eventRepository) {
        this.accountManager = accountManager;
        this.eventRepository = eventRepository;
    }

    public void fetchAccountInfo() {
        accountManager.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        Log.i(TAG, "Name of user: " + user.getFirstName() + " " + user.getLastName());
                        Log.i(TAG, user.toString());
                        if(isViewAttached() && getView() != null) {
                            Log.d(TAG, "logout: notifying the view");
                            getView().onUserHasLoggedInEvent(null);
                        }
                    }
                });
    }

    public void logout() {
        Log.d(TAG, "logout: logging out");
        accountManager.logout();
        if(isViewAttached() && getView() != null) {
            Log.d(TAG, "logout: notifying the view");
            getView().onUserHasLoggedOutEvent();
        }
    }

    public void onLoginState() {
        if(isViewAttached() && getView() != null) {
            if(accountManager.isLoggedIn()) {
                fetchAccountInfo();
                getView().viewAuthenticatedNavigation();
            } else {
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
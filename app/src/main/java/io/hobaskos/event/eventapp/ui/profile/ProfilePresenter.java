package io.hobaskos.event.eventapp.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.AccountRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Magnus on 22.02.2017.
 */

public class ProfilePresenter extends MvpBasePresenter<ProfileView> {

    private AccountManager accountManager;
    private AccountRepository accountRepository;

    @Inject
    public ProfilePresenter(AccountManager accountManager, AccountRepository accountRepository) {
        this.accountManager = accountManager;
        this.accountRepository = accountRepository;
    }

    public void refreshProfileData()
    {
        accountManager.getAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        if(isViewAttached())
                        {
                            getView().setProfileData(user);
                        }
                    }
                });
    }

    public void getEventUserAttending() {
        accountRepository.getAttendingEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Event>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Event> events) {
                        getView().setEventAttending(events);
                    }
                });
    }

}

package io.hobaskos.event.eventapp.ui.profile;

import com.google.android.gms.games.social.Social;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.User;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Magnus on 08.03.2017.
 */

public class ProfileEditPresenter extends MvpBasePresenter<ProfileEditView> {
    private AccountManager accountManager;

    @Inject
    public ProfileEditPresenter (AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public void refreshProfileData() {
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
                        if (isViewAttached()) {
                            getView().setProfileData(user);
                            getView().updateProfileData(user);
                        }
                    }
                });
    }


}//End of class


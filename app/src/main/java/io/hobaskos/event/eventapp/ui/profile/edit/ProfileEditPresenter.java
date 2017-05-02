package io.hobaskos.event.eventapp.ui.profile.edit;

import android.util.Log;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.User;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Magnus on 08.03.2017.
 */
public class ProfileEditPresenter extends MvpBasePresenter<ProfileEditView> {

    public static final String TAG = ProfileEditActivity.class.getName();
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
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        Toast.makeText(App.getInst(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(User user) {
                        if (isViewAttached()) {
                            getView().setProfileData(user);
                        }
                    }
                });
    }

    public void updateProfile(User user) {
        accountManager.saveAccount(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        Toast.makeText(App.getInst(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void v) {
                        if (isViewAttached()) {
                            getView().savedProfileData();

                        }
                    }
                });
    }
}


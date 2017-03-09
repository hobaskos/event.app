package io.hobaskos.event.eventapp.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpView;

import io.hobaskos.event.eventapp.data.model.User;

/**
 * Created by Magnus on 08.03.2017.
 */

public interface ProfileEditView extends MvpView {
    void setProfileData(User user);
    void updateProfileData();
}
package io.hobaskos.event.eventapp.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by hansp on 25.02.2017.
 */

public interface LoginView extends MvpView {
    void hasNotLoggedInSuccessfully();
    void hasLoggedInSuccessfully();
}

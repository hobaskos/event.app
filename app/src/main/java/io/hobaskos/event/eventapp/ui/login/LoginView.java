package io.hobaskos.event.eventapp.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpView;

import io.hobaskos.event.eventapp.data.model.response.Response;

/**
 * Created by hansp on 25.02.2017.
 */

public interface LoginView extends MvpView {
    void showLoginForm();
    void showError(Response response);
    void showSuccess(Response response);
}

package io.hobaskos.event.eventapp.ui.login;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;

/**
 * Created by andre on 2/15/2017.
 */

public interface LoginView extends MvpView {

    void showLoginForm();
    void showError(Response response);
    void showSuccess(Response response);

}

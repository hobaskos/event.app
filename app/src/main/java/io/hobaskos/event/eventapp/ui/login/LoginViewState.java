package io.hobaskos.event.eventapp.ui.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.events.EventsView;
import io.hobaskos.event.eventapp.ui.events.EventsViewState;

/**
 * Created by osvold.hans.petter on 15.02.2017.
 */

public class LoginViewState implements ViewState<LoginView> {

    private final static String TAG = LoginViewState.class.getName();

    private final int STATE_SHOW_LOGIN_FORM = 0;
    private final int STATE_SHOW_ERROR = 1;
    private final int STATE_SHOW_SUCCESS = 2;

    private int state = STATE_SHOW_LOGIN_FORM;

    private Response response;

    @Override
    public void apply(LoginView view, boolean retained) {

        switch (state)
        {
            case STATE_SHOW_LOGIN_FORM:
                view.showLoginForm();
                break;

            case STATE_SHOW_ERROR:
                view.showError(response);
                break;

            case STATE_SHOW_SUCCESS:
                view.showSuccess(response);
                break;

            default:
                view.showLoginForm();
                break;
        }
    }

    void setShowLoginForm() {
        state = STATE_SHOW_LOGIN_FORM;
    }

    void setShowError() {
        state = STATE_SHOW_ERROR;
    }

    void setShowSuccess() {
        state = STATE_SHOW_SUCCESS;
    }

    public void setResponse(Response response)
    {
        this.response = response;
    }

}

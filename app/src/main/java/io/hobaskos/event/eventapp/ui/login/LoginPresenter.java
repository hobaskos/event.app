package io.hobaskos.event.eventapp.ui.login;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;


/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginPresenter extends MvpBasePresenter<LoginView> {

    private JwtRepository repository;

    @Inject
    public LoginPresenter(JwtRepository repository) {
        this.repository = repository;
    }

    public void login(String login, String password, boolean rememberMe)
    {
        LoginVM loginVM = new LoginVM(login, password, rememberMe);

        repository.login(loginVM, this);
    }

    public void callbackOnSuccess()
    {
        Log.i("loginPresenter", "callback success");

        Response response = new Response(true, "Success");

        if(isViewAttached())
        {
            getView().showSuccess(response);
        }

    }

    public void callbackOnError(Throwable throwable)
    {
        Log.i("loginPresenter", "callbackError(" + throwable.getMessage() + ")");

        Response response = new Response(false, throwable.getMessage());

        if(isViewAttached())
        {
            getView().showError(response);
        }

    }
}

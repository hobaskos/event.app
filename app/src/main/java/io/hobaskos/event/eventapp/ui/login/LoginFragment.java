package io.hobaskos.event.eventapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.Arrays;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.base.view.fragment.BaseViewStateFragment;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by osvold.hans.petter on 15.02.2017.
 */

public class LoginFragment extends BaseViewStateFragment<LoginView, LoginPresenter>
    implements LoginView {

    @Inject
    public LoginPresenter loginPresenter;

    public final static String TAG = LoginActivity.class.getName();

    private TextView title;
    private EditText fieldEmail;
    private EditText fieldPassword;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.login_title);
        fieldEmail = (EditText) view.findViewById(R.id.field_email);
        fieldEmail.setHint(R.string.email);
        fieldPassword = (EditText) view.findViewById(R.id.field_password);
        fieldPassword.setHint(R.string.password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setText(R.string.login);
        addListenerOnButton();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @NonNull
    @Override
    public ViewState createViewState() {
        return new LoginViewState();
    }

    @Override
    public void onNewViewStateInstance() {
        showLoginForm();
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return loginPresenter;
    }

    @Override
    public void showLoginForm() {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowLoginForm();

        //
    }

    @Override
    public void showError(Response response) {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowError();
        loginViewState.setResponse(response);

        Log.i("login-activity", response.getMessage());
        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(Response response) {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowSuccess();
        loginViewState.setResponse(response);

        Log.i("login-activity", response.getMessage());
        Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void addListenerOnButton()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login(fieldEmail.getText().toString(), fieldEmail.getText().toString());
            }
        });
    }
}

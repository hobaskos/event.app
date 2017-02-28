package io.hobaskos.event.eventapp.ui.login;

/**
 * Created by hansp on 25.02.2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
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

    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;
    private LoginButton btnFacebook;
    private Button btnSkip;

    private CallbackManager callbackManager;


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

        etLogin = (EditText) view.findViewById(R.id.field_username);
        etPassword = (EditText) view.findViewById(R.id.field_password);

        btnLogin = (Button) getView().findViewById(R.id.btn_login_username);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        initFacebookLogin();


        btnSkip = (Button) view.findViewById(R.id.btn_skip_login);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

    private void initFacebookLogin()
    {
        callbackManager = CallbackManager.Factory.create();

        btnFacebook = (LoginButton) getView().findViewById(R.id.btn_login_facebook);
        btnFacebook.setReadPermissions("email");
        btnFacebook.setFragment(this);
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("Login Token", loginResult.getAccessToken().getToken());
                Log.i("Login UserId", loginResult.getAccessToken().getUserId());

                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();

                SocialUserVM socialUserVM = new SocialUserVM(userId, token);
                presenter.login(socialUserVM);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("Error:", error.toString());
            }
        });
    }

    private void login()
    {
        if(!validate())
        {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getText(R.string.authenticating));
        progressDialog.show();

        LoginVM loginVM = new LoginVM(etLogin.getText().toString(), etPassword.getText().toString());

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.login(loginVM);
                progressDialog.dismiss();
            }
        }, 3000);

    }

    public boolean validate() {
        boolean valid = true;

        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();

        if (login.isEmpty() ) {
            etLogin.setError(getText(R.string.login_username_empty));
            valid = false;
        } else {
            etLogin.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError(getText(R.string.login_password_error));
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void onLoginFailed()
    {
        Toast.makeText(getContext(), getText(R.string.login_failure), Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
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
        Toast.makeText(getActivity(), R.string.login_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(Response response) {
        LoginViewState loginViewState = (LoginViewState) viewState;
        loginViewState.setShowSuccess();
        loginViewState.setResponse(response);
        Toast.makeText(getActivity(), R.string.login_success, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

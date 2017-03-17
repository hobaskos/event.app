package io.hobaskos.event.eventapp.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.SocialType;
import io.hobaskos.event.eventapp.data.model.SocialUserVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by hansp on 24.02.2017.
 */

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements LoginView {

    @Inject
    public LoginPresenter presenter;

    private EditText etLogin;
    private EditText etPassword;
    private Button btnLogin;

    private CallbackManager callbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);

        etLogin = (EditText) findViewById(R.id.field_username);
        etPassword = (EditText) findViewById(R.id.field_password);

        btnLogin = (Button) findViewById(R.id.btn_login_username);
        btnLogin.setOnClickListener(v -> login());

        initFacebookLogin();

        TextView linkSkip = (TextView) findViewById(R.id.link_skip_login);
        linkSkip.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), MainActivity.class)));
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        App.getInst().getComponent().inject(this);
        return presenter;
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginButton btnFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();

                SocialUserVM socialUserVM = new SocialUserVM(userId, token);
                socialUserVM.setType(SocialType.FACEBOOK);
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

    private void login() {
        if(!validate())
        {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getText(R.string.authenticating));
        progressDialog.show();

        LoginVM loginVM = new LoginVM(etLogin.getText().toString(), etPassword.getText().toString());

        new android.os.Handler().postDelayed(() -> {
            presenter.login(loginVM);
            progressDialog.dismiss();
        }, 2000);

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

        if (password.isEmpty()) {
            etPassword.setError(getText(R.string.login_password_error));
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void onLoginFailed() {
        Toast.makeText(this, getText(R.string.login_failure), Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }


    @Override
    public void showLoginForm() {

    }

    @Override
    public void showError(Response response) {
        btnLogin.setEnabled(true);
        Toast.makeText(this, R.string.login_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccess(Response response) {
        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

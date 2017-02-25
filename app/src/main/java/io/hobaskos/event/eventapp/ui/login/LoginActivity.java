package io.hobaskos.event.eventapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.repository.JwtRepository;
import io.hobaskos.event.eventapp.data.service.JwtStorageProxy;
import io.hobaskos.event.eventapp.ui.main.MainActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 24.02.2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Inject
    public JwtRepository repository;

    @Inject
    public JwtStorageProxy jwtStorageProxy;

    private CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        App.getInst().getComponent().inject(this);

        initRegularLogin();
        initFacebookLogin();

        Button btnSkip = (Button) findViewById(R.id.btn_skip_login);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void initRegularLogin()
    {
        etEmail = (EditText) findViewById(R.id.field_email);
        etEmail.setHint(R.string.email);

        etPassword = (EditText) findViewById(R.id.field_password);
        etPassword.setHint(R.string.password);

        Button btnLogin = (Button) findViewById(R.id.btn_login_username);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginVM loginVM = new LoginVM(etEmail.getText().toString(), etPassword.getText().toString());

                repository.login(loginVM).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("OnError:", e.getMessage());
                        Log.i("OnError:", e.toString());
                        onLoginError();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        onLoginSuccess();
                    }
                });

            }
        });
    }

    private void initFacebookLogin()
    {
        callbackManager = CallbackManager.Factory.create();

        LoginButton btnFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);

        // Todo: Fetch more info from facebook.
        btnFacebook.setReadPermissions("email");
        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LoginActivity", "OnSuccess(token)");
                Log.i("Token", loginResult.getAccessToken().getToken());
                Log.i("UserId", loginResult.getAccessToken().getUserId());

                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();

                // Todo: create SocialUserVm.

                jwtStorageProxy.put(token);

                // TODO: Push SocialUserVM to server.

                onLoginSuccess();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LoginActivity", "onError(error)");
                Log.i("Error:", error.toString());
                onLoginError();
            }
        });
    }

    public void onLoginSuccess()
    {
        Toast.makeText(getBaseContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onLoginError()
    {

        Toast.makeText(getBaseContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

package io.hobaskos.event.eventapp.ui;

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

public class SplashActivity extends AppCompatActivity {

    private Button button_login_username_password, skipButton;
    private EditText fieldEmail;
    private EditText fieldPassword;

    @Inject
    public JwtRepository repository;

    @Inject
    public JwtStorageProxy jwtStorageProxy;

    // Facebook stuff
    private LoginButton button_login_facebook;
    private CallbackManager callbackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        App.getInst().getComponent().inject(this);

        initRegularLogin();
        initFacebookLogin();

        skipButton = (Button) findViewById(R.id.btn_skip_login);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void initRegularLogin()
    {
        fieldEmail = (EditText) findViewById(R.id.field_email);
        fieldEmail.setHint(R.string.email);

        fieldPassword = (EditText) findViewById(R.id.field_password);
        fieldPassword.setHint(R.string.password);

        button_login_username_password = (Button) findViewById(R.id.btn_login_username);

        button_login_username_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginVM loginVM = new LoginVM(fieldEmail.getText().toString(), fieldPassword.getText().toString());

                repository.login(loginVM).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Toast.makeText(getBaseContext(), R.string.login_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });

            }
        });
    }

    private void initFacebookLogin()
    {
        callbackManager = CallbackManager.Factory.create();

        button_login_facebook = (LoginButton) findViewById(R.id.btn_login_facebook);

        button_login_facebook.setReadPermissions("email");

        button_login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("SplashActivity", "OnSuccess(token)");
                Log.i("Token", loginResult.getAccessToken().getToken());
                Log.i("UserId", loginResult.getAccessToken().getUserId());

                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();

                jwtStorageProxy.put(token);

                Toast.makeText(getBaseContext(), R.string.login_success, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("SplashActivity", "onError(error)");
                Log.i("Error:", error.toString());
                Toast.makeText(getBaseContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

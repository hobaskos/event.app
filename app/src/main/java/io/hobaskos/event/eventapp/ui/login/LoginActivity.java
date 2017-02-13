package io.hobaskos.event.eventapp.ui.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.UserLogin;
import io.hobaskos.event.eventapp.ui.base.PresenterFactory;
import rx.Observer;

import io.hobaskos.event.eventapp.ui.base.BaseMvpActivity;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements Observer<UserLogin> {

    @Inject
    public LoginPresenter loginPresenter;

    public final static String TAG = LoginActivity.class.getName();

    private TextView title;
    private EditText field_login;
    private EditText field_password;
    private Button btn_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        title = (TextView) findViewById(R.id.login_title);
        title.setText("TEST");

        field_login = (EditText) findViewById(R.id.field_login);
        field_password = (EditText) findViewById(R.id.field_password);

        btn_login = (Button) findViewById(R.id.btn_login);

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Login button clicked!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    protected String tag() {
        return TAG;
    }

    @NonNull
    @Override
    protected PresenterFactory<LoginPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> loginPresenter;
    }

    @Override
    protected void onPresenterPrepared(@NonNull LoginPresenter presenter) {
        Log.i(TAG, "onPresenterPrepared");
        this.loginPresenter = presenter;
        loginPresenter.subscribe(this);
    }

    @Override
    public void onCompleted() {
        title.setText("blah");
    }

    @Override
    public void onError(Throwable e) {
        Log.i("login-activity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(UserLogin user) {
        Log.i("login-activity", "LoginActivity.onNext");
        Toast.makeText(this, "LoginActivity.onNext", Toast.LENGTH_SHORT).show();
    }
}

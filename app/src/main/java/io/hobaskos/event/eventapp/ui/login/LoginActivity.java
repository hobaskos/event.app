package io.hobaskos.event.eventapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseLceViewStateActivity;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginActivity extends BaseLceViewStateActivity<LinearLayout, Event, LoginView, LoginPresenter>
        implements LoginView {

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

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return null;
    }

    private void addListenerOnButton()
    {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login(field_login.getText().toString(), field_password.getText().toString(), false);
            }
        });


    }


    /*
    @NonNull
    @Override
    protected PresenterFactory<LoginPresenter> getPresenterFactory() {
        App.getInst().getComponent().inject(this);
        return () -> loginPresenter;
    }
    */
    /*
    protected void onPresenterPrepared(@NonNull LoginPresenter presenter) {
        Log.i(TAG, "onPresenterPrepared");
        this.loginPresenter = presenter;
        loginPresenter.subscribe(this);
    }
    */


    public void onCompleted() {
        title.setText("blah");
    }


    public void onError(Throwable e) {
        Log.i("login-activity", e.getMessage());
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }


    public void onNext(rx.Notification<Response> notification) {
        Log.i("login-activity", "LoginActivity.onNext");

        Response response = notification.getValue();

        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

        if(response.getStatus())
        {
            startActivity(new Intent(this, MainActivity.class));
        }


    }

    @Override
    public LceViewState<Event, LoginView> createViewState() {
        return null;
    }

    @Override
    public Event getData() {
        return null;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(Event data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}

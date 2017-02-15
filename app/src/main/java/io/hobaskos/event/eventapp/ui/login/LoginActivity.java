package io.hobaskos.event.eventapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.LoginVM;
import io.hobaskos.event.eventapp.data.model.response.Response;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseActivity;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseLceViewStateActivity;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by osvold.hans.petter on 13.02.2017.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .commit();
        }
    }


/*    public void onError(Throwable e) {
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
    }*/

}

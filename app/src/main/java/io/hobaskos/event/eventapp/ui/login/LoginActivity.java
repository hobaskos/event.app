package io.hobaskos.event.eventapp.ui.login;

import android.os.Bundle;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.base.view.activity.BaseActivity;

/**
 * Created by hansp on 24.02.2017.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .commit();
        }
    }

}

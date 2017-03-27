package io.hobaskos.event.eventapp.ui.profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.hobaskos.event.eventapp.R;

public class ProfileFragmentActivity extends AppCompatActivity {

    private final static String PROFILE_FRAGMENT_TAG = "profileFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile_activity_wrapper, new ProfileFragment(), PROFILE_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

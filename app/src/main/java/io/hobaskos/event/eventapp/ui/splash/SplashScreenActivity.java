package io.hobaskos.event.eventapp.ui.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.ActivityCompat;

import com.google.gson.JsonParser;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by test on 3/26/2017.
 */

public class SplashScreenActivity extends Activity {

    // Permissions:
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    // Permission Request:
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request permissions:
        ActivityCompat.requestPermissions(SplashScreenActivity.this, INITIAL_PERMS, INITIAL_REQUEST);

    }

    //TODO: More advanced request handling
    // http://stackoverflow.com/questions/33266328/how-can-i-customize-permission-dialog-in-android
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // Start MainActivity after all preconfiguration and loading is done
        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(i);

        // close this activity
        finish();
    }



}
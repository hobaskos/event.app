package io.hobaskos.event.eventapp.ui.event.search.map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.ui.event.details.MapsActivity;
import io.hobaskos.event.eventapp.ui.event.search.SearchEventsFragment;

/**
 * Created by test on 3/15/2017.
 */

public class SearchEventsMapActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_map);

        setTitle(R.string.events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_pane, new SearchEventsMapFragment())
                    .commit();
        }
    } // end of onCreate()

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    } // end of onOptionsItemSelected()

}

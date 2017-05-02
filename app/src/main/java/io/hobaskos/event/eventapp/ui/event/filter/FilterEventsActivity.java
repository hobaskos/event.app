package io.hobaskos.event.eventapp.ui.event.filter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.hobaskos.event.eventapp.R;

/**
 * Created by test on 3/15/2017.
 */
@Deprecated
public class FilterEventsActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_events);

        setTitle(R.string.filter_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_pane, new FilterEventsFragment())
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


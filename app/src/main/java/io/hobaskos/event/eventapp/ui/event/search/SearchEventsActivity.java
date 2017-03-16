package io.hobaskos.event.eventapp.ui.event.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.hobaskos.event.eventapp.R;

/**
 * Created by test on 3/13/2017.
 */

public class SearchEventsActivity extends AppCompatActivity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_events);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_pane, new SearchEventsFragment())
                    .commit();
        }
    }
}

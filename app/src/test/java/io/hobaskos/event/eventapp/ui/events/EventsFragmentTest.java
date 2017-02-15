package io.hobaskos.event.eventapp.ui.events;

import android.support.v7.widget.RecyclerView;
import android.test.UiThreadTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by andre on 2/15/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
public class EventsFragmentTest {


    private MainActivity mainActivity;
    private EventsFragment fragment;

    RecyclerView recyclerView;
    EventsAdapter adapter;
    List<Event> eventList;


    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    @UiThreadTest
    public void basicViewTest() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .get();

        fragment = new EventsFragment();
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_pane, fragment).commit();

        assertNotNull(fragment);
    }

    public void testRecyclerView() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .get();

        fragment = new EventsFragment();
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_pane, fragment).commit();
        assertNotNull(fragment);

        recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.recyclerView);
        // Manually measure and lay out recyclerView:
        recyclerView.measure(0, 0);
        recyclerView.layout(0, 0, 100, 10000);

        assertNotNull(recyclerView);

        adapter = (EventsAdapter) recyclerView.getAdapter();

        eventList = adapter.getItems();

        Event event = eventList.get(0);
        Long id = event.getId();
        Long idToMatch = 1L;
        assertEquals(id, idToMatch);
    }
}

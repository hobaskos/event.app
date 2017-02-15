package io.hobaskos.event.eventapp.ui.events;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.ui.main.MainActivity;

/**
 * Created by andre on 2/15/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestApp.class)
@Ignore
public class EventsFragmentTest {

    private MainActivity mainActivity;
    private EventsFragment eventsFragment = new EventsFragment();
    private EventsPresenter presenter;

    @Before
    public void setup() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .get();

        SupportFragmentTestUtil.startVisibleFragment(eventsFragment);

        presenter = eventsFragment.getPresenter();
    }

    @Test
    public void basicViewTest() {

    }
}

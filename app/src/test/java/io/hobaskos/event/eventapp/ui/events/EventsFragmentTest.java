package io.hobaskos.event.eventapp.ui.events;

import android.test.UiThreadTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.ui.main.MainActivity;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;

/**
 * Created by andre on 2/15/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
public class EventsFragmentTest {

    private MainActivity mainActivity;
    private EventsFragment eventsFragment = new EventsFragment();
    private EventsPresenter presenter;

    @Before
    public void setup() {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });

        //startFragment(eventsFragment);

        //presenter = eventsFragment.getPresenter();
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    @UiThreadTest
    public void basicViewTest() {
        mainActivity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .start()
                .get();

        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_pane, eventsFragment).commit();

        assertNotNull(eventsFragment);
    }
}

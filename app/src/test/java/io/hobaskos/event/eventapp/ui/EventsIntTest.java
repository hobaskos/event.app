package io.hobaskos.event.eventapp.ui;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.events.EventsActivity;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertTrue;


/**
 * Created by alex on 1/29/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
@Ignore
public class EventsIntTest {

    private EventsActivity eventsActivity;

    private TestSubscriber<List<Event>> testSubscriber = new TestSubscriber<>();

    @Before
    public void setup() {

        eventsActivity = Robolectric.buildActivity(EventsActivity.class)
                .create()
                .start()
                .get();

        eventsActivity.eventsPresenter.getObservable().subscribe(testSubscriber);
    }

    @Test
    public void basicViewTest() {

        testSubscriber.assertNoErrors();
        testSubscriber.onCompleted();
        testSubscriber.assertCompleted();

        assertTrue(eventsActivity.getEventsList().size() == 1);
    }
}

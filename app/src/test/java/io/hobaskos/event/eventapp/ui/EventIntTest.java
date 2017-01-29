package io.hobaskos.event.eventapp.ui;

import android.content.Intent;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.R;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.event.EventActivity;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


/**
 * Created by alex on 1/29/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23, application = TestApp.class)
public class EventIntTest {

    private EventActivity eventActivity;

    private TestSubscriber<Event> testSubscriber = new TestSubscriber<>();

    @Before
    public void setup() {

        Intent intent = new Intent();
        intent.putExtra(EventActivity.EVENT_ID, 1);

        eventActivity = Robolectric.buildActivity(EventActivity.class)
                .withIntent(intent)
                .create()
                .start()
                .get();

        eventActivity.eventPresenter.getObservable().subscribe(testSubscriber);
    }

    @Test
    public void basicViewTest() {

        testSubscriber.assertNoErrors();
        testSubscriber.onCompleted();
        testSubscriber.assertCompleted();

        TextView eventTitle = (TextView) eventActivity.findViewById(R.id.event_title);
        assertThat(eventTitle.getText().toString(), equalTo("event1"));
    }
}

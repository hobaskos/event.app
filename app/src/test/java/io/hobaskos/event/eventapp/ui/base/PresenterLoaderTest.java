package io.hobaskos.event.eventapp.ui.base;

/**
 * Created by andre on 2/9/2017.
 */


import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.events.EventsPresenter;
import io.hobaskos.event.eventapp.util.SupportLoaderTestCase;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestApp.class)
public class PresenterLoaderTest extends SupportLoaderTestCase {

    String tag = "test";

    @Mock
    EventRepository eventRepositoryMock;

    @Mock
    EventsPresenter eventsPresenterMock;

    /**
     * Test that given a presenterfactory, the loader returns a presenter
     */
    @Test
    public void testPresenterFactory() {
        MockitoAnnotations.initMocks(this);


        //EventsPresenter presenter = new EventsPresenter(eventRepositoryMock);

        PresenterFactory<EventsPresenter> eventsPresenterPresenterFactory = new PresenterFactory<EventsPresenter>() {
            @NonNull
            @Override
            public EventsPresenter create() {
                return eventsPresenterMock;
            }
        };

        PresenterLoader<EventsPresenter> spyPresenterLoader = Mockito.spy(
                new PresenterLoader(RuntimeEnvironment.application, eventsPresenterPresenterFactory,
                        tag));

        EventsPresenter presenter2 = getLoaderResultSynchronously(spyPresenterLoader);

        Assert.assertNotNull(presenter2);
        Assert.assertEquals(eventsPresenterMock, presenter2);

    }



}

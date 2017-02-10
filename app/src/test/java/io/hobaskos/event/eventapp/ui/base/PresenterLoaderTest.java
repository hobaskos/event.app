package io.hobaskos.event.eventapp.ui.base;

/**
 * Created by andre on 2/9/2017.
 */


import android.support.annotation.NonNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.hobaskos.event.eventapp.BuildConfig;
import io.hobaskos.event.eventapp.TestApp;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.repository.EventRepositoryTest;
import io.hobaskos.event.eventapp.ui.events.EventsPresenter;
import io.hobaskos.event.eventapp.util.SupportLoaderTestCase;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = TestApp.class)
public class PresenterLoaderTest extends SupportLoaderTestCase {

    String tag = "test";

    /**
     * Test that given a presenterfactory, the loader returns a presenter
     */
    @Test
    public void testPresenterFactory() {
        PresenterFactory<EventsPresenter> eventsPresenterPresenterFactory = new PresenterFactory<EventsPresenter>() {
            @NonNull
            @Override
            public EventsPresenter create() {
                return new EventsPresenter(new EventRepository());
            }
        };

        PresenterLoader<EventsPresenter> spyPresenterLoader = setupPresenterLoaderForTest(
                new PresenterLoader(RuntimeEnvironment.application, eventsPresenterPresenterFactory,
                        tag));


    }

    private PresenterLoader setupPresenterLoaderForTest(PresenterLoader loader) {
        PresenterLoader spyPresenterLoader = Mockito.spy(loader);

        /*
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String
            }
        })
        */
        return spyPresenterLoader;
    }


}

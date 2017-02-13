package io.hobaskos.event.eventapp.ui.events;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.events.old.EventsPresenter;
import io.hobaskos.event.eventapp.ui.events.old.EventsView;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by andre on 2/10/2017.
 */
/*
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = 23, application = TestApp.class)
*/
public class EventsPresenterTest {

    private List<Event> eventList;

    //@Mock
    //private EventRepository eventRepository;

    @Mock
    private EventsView mockEventsView;

    @Captor
    private ArgumentCaptor<Observable> observableArgumentCaptor;

    private EventsPresenter eventsPresenter;

    @Before
    public void setup() {
        // Inject mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);
        // Initialize class to be tested
        //eventsPresenter = new EventsPresenter(eventRepository);
        //eventsPresenter.onAttachView(mockEventsView);
        eventList = new ArrayList<>();

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void testRefreshData() {
        EventRepository eventRepository =  mock(EventRepository.class);

        eventsPresenter = new EventsPresenter(eventRepository);
        eventsPresenter.onAttachView(mockEventsView);

        when(eventRepository.getPages(1)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(eventList);
            subscriber.onCompleted();
        }));

        eventsPresenter.refreshData();

        InOrder inOrder = inOrder(mockEventsView);

        inOrder.verify(mockEventsView, times(1)).showLoading(true);
        inOrder.verify(mockEventsView, times(1)).setData(anyListOf(Event.class));
        inOrder.verify(mockEventsView, times(1)).showLoading(false);

        verify(mockEventsView, never()).showError(any(Throwable.class));
    }
}

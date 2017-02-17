package io.hobaskos.event.eventapp.ui.event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by andre on 2/16/2017.
 */

public class EventPresenterTest {
    @Mock private EventRepository eventRepository;
    @Mock private EventView view;
    private EventPresenter eventPresenter;

    boolean pullToRefresh = false;

    @Before
    public void setup() {
        // Inject mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);
        // Initialize class to be tested
        eventPresenter = new EventPresenter(eventRepository);
        // Override RxJava schedulers
        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnComputationScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.immediate();
            }
        });

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxJavaHooks.reset();
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void testAttachView() {
        eventPresenter.attachView(view);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testGetView() {
        assertEquals("Test before view attached", null, eventPresenter.getView());
        eventPresenter.attachView(view);
        assertEquals("Test with view attached", view, eventPresenter.getView());
        eventPresenter.detachView(true);
        assertEquals("Test after view detached", null, eventPresenter.getView());
    }

    @Test
    public void testGetEventSuccess() {
        Event event = new Event();
        Long id = 1L;
        when(eventRepository.get(id)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(event);
            subscriber.onCompleted();
        }));

        eventPresenter.attachView(view);
        eventPresenter.getEvent(id);

        verify(view, never()).showError(any(Exception.class), anyBoolean());
        verify(view, times(1)).showLoading(pullToRefresh);
        verify(view, times(1)).setData(any(Event.class));
        verify(view, times(1)).showContent();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testGetEventError() {
        Long id = 1L;
        when(eventRepository.get(id)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(new Exception());
            subscriber.onCompleted();
        }));

        eventPresenter.attachView(view);
        eventPresenter.getEvent(id);

        verify(view, times(1)).showLoading(pullToRefresh);
        verify(view, times(1)).showError(any(Exception.class), Matchers.eq(pullToRefresh));
        verify(view, never()).showContent();
        verify(view, never()).setData(any(Event.class));
        verifyNoMoreInteractions(view);
    }


}

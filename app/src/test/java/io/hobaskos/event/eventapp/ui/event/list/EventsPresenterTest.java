package io.hobaskos.event.eventapp.ui.event.list;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by andre on 2/15/2017.
 */

public class EventsPresenterTest {

    private List<EventsPresentationModel> eventsPresentationModelList;


    private List<Event> eventList;
    @Mock private EventRepository eventRepository;
    @Mock private PersistentStorage persistentStorage;
    @Mock private EventsView view;
    private EventsPresenter eventsPresenter;


    @Before
    public void setup() {
        // Inject mocks with the @Mock annotation.
        MockitoAnnotations.initMocks(this);
        // Initialize class to be tested
        eventsPresenter = new EventsPresenter(eventRepository, persistentStorage);
        eventList = new ArrayList<>();
        eventsPresentationModelList = new ArrayList<>();

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
        eventsPresenter.attachView(view);
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testLoadEventsSuccess() {

        when(eventRepository.search(anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(eventList);
            subscriber.onCompleted();
        }));

        boolean pullToRefresh = true;
        eventsPresenter.attachView(view);
        eventsPresenter.loadEvents(pullToRefresh);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view, times(1)).showLoadMore(false);
        inOrder.verify(view, times(1)).showLoading(pullToRefresh);
        inOrder.verify(view, times(1)).setData(anyListOf(EventsPresentationModel.class));
        inOrder.verify(view, times(1)).showContent();
        verify(view, never()).showError(any(Exception.class), anyBoolean());
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testLoadEventsError() {
        when(eventRepository.search(anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(new Exception());
        }));

        boolean pullToRefresh = true;
        eventsPresenter.attachView(view);
        eventsPresenter.loadEvents(pullToRefresh);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view, times(1)).showLoadMore(false);
        inOrder.verify(view, times(1)).showLoading(pullToRefresh);
        inOrder.verify(view, times(1)).showError(any(Exception.class), Matchers.eq(pullToRefresh));
        verify(view, never()).showContent();
        verify(view, never()).setData(anyListOf(EventsPresentationModel.class));
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testLoadMoreEventsSuccess() {
        int page = 1;
        when(eventRepository.search(anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(eventList);
            subscriber.onCompleted();
        }));

        eventsPresenter.attachView(view);
        eventsPresenter.loadMoreEvents(page);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view, times(1)).showLoadMore(true);
        inOrder.verify(view, times(1)).addMoreData(anyListOf(EventsPresentationModel.class));
        inOrder.verify(view, times(1)).showLoadMore(false);
        verify(view, never()).showLoadMoreError(any(Exception.class));
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testLoadMoreEventsError() {
        int page = 1;
        when(eventRepository.search(anyInt(), anyDouble(), anyDouble(), anyString())).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(new Exception());
        }));

        eventsPresenter.attachView(view);
        eventsPresenter.loadMoreEvents(page);

        InOrder inOrder = inOrder(view);
        inOrder.verify(view, times(1)).showLoadMore(true);
        inOrder.verify(view, times(1)).showLoadMoreError(any(Exception.class));
        inOrder.verify(view, times(1)).showLoadMore(false);
        verify(view, never()).addMoreData(anyListOf(EventsPresentationModel.class));
        verifyNoMoreInteractions(view);
    }
}

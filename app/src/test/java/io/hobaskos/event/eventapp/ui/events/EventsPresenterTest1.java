package io.hobaskos.event.eventapp.ui.events;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.AbsMvpLcePresenterTest;
import rx.Observable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by andre on 2/15/2017.
 */

public class EventsPresenterTest1
        extends AbsMvpLcePresenterTest<List<Event>, EventsView, EventsPresenter> {

    private List<Event> eventList;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventsView mockEventsView;


    private EventsPresenter eventsPresenter;

    @BeforeClass
    public void classSetup() {

    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Override
    protected void beforeTestNotFailing() {
        when(eventRepository.getPages(1)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onNext(eventList);
            subscriber.onCompleted();
        }));
    }

    @Override
    protected void beforeTestFailing() {
        when(eventRepository.getPages(1)).thenReturn(Observable.create((subscriber) -> {
            subscriber.onError(new Throwable("test"));
        }));
    }

    @Override
    protected Class<EventsView> getViewClass() {
        return EventsView.class;
    }

    @Override
    protected EventsPresenter createPresenter() {
        eventRepository =  mock(EventRepository.class);
        eventsPresenter = new EventsPresenter(eventRepository);
        return eventsPresenter;
    }

    @Test
    public void testLce() {
        super.startLceTests(true);
    }


    @Override
    protected void loadData(EventsPresenter presenter, boolean pullToRefresh) {
        presenter.loadEvents(pullToRefresh);
    }

    @Override
    protected void verifyData(List<Event> data) {

    }
}

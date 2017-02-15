package io.hobaskos.event.eventapp.module;

import java.util.List;

import dagger.Module;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.model.Event;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import rx.Observable;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 1/29/17.
 */
@Module
public class TestNetModule extends NetModule {

    private Event event;

    private List<Event> eventList;

    public TestNetModule(HttpUrl httpUrl) {
        super(httpUrl);
    }

    @Override
    public EventService.Anonymously providesEventServiceAnon(Cache cache) {

        EventService.Anonymously eventService = mock(EventService.Anonymously.class);

        when(eventService.getEvent(anyLong())).thenReturn(Observable.just(event));

        when(eventService.getEvents(anyInt(), anyInt())).thenReturn(Observable.just(eventList));

        return eventService;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}

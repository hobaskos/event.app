package io.hobaskos.event.eventapp.repository;

import java.io.IOException;
import java.util.List;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.api.ApiService;
import io.hobaskos.event.eventapp.api.EventApi;
import io.hobaskos.event.eventapp.models.Event;
import rx.Observable;

/**
 * Created by andre on 1/25/2017.
 */
public class EventRepository {

    public Observable<List<Event>> getEvents() throws IOException {

        EventApi api = ApiService.build(App.getInst().getApiUrl())
                .createService(EventApi.class);
        return api.getEvents();
    }
}

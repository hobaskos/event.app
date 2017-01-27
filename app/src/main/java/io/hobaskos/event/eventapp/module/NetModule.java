package io.hobaskos.event.eventapp.module;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import okhttp3.HttpUrl;

/**
 * Created by alex on 1/26/17.
 */
@Module
public class NetModule {

    protected HttpUrl httpUrl;

    @Inject
    public NetModule(HttpUrl httpUrl) {
        this.httpUrl = httpUrl;
    }

    @Singleton
    @Provides
    public EventService providesEventService() {
        return ApiService.build(httpUrl).createService(EventService.class);
    }

    @Singleton
    @Provides
    public EventRepository providesEventRepository(EventService eventService)
    {
        return new EventRepository(eventService);
    }
}

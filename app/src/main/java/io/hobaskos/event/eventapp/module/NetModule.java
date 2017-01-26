package io.hobaskos.event.eventapp.module;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.api.ApiService;
import io.hobaskos.event.eventapp.api.EventApi;
import io.hobaskos.event.eventapp.repository.EventRepository;
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
    public EventApi providesEventApi() {
        return ApiService.build(httpUrl).createService(EventApi.class);
    }

    @Singleton
    @Provides
    public EventRepository providesEventRepository(EventApi eventApi)
    {
        return new EventRepository(eventApi);
    }
}

package io.hobaskos.event.eventapp.module;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.PersistentStorage;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.api.JWTTokenIntercepter;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import okhttp3.Cache;
import okhttp3.HttpUrl;

/**
 * Created by alex on 1/26/17.
 */
@Module
public class NetModule {

    protected HttpUrl httpUrl;
    private int cacheSize = 10 * 1024 * 1024; // 10 MiB

    @Inject
    public NetModule(HttpUrl httpUrl) {
        this.httpUrl = httpUrl;
    }

    @Provides
    @Singleton
    public Cache providesCache(App app) {
        return new Cache(app.getCacheDir(), cacheSize);
    }

    @Singleton
    @Provides
    public EventService.Anonymously providesEventServiceAnon(Cache cache) {
        return ApiService.build(httpUrl).createService(EventService.Anonymously.class, cache);
    }

    @Singleton
    @Provides
    public EventService.Authenticated providesEventServiceAuthenticated(Cache cache, JWTTokenIntercepter intercepter) {
        return ApiService.build(httpUrl).createService(EventService.Authenticated.class, cache, intercepter);
    }

    @Singleton
    @Provides
    public EventRepository providesEventRepository(EventService.Anonymously eventServiceAnonymously, EventService.Authenticated eventServiceAuthenticated) {
        return new EventRepository(eventServiceAnonymously, eventServiceAuthenticated);
    }

    @Singleton
    @Provides
    public JWTTokenIntercepter providesIntercepter(PersistentStorage storage) {
        return new JWTTokenIntercepter(storage);
    }
}

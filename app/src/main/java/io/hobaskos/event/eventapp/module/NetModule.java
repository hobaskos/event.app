package io.hobaskos.event.eventapp.module;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventCategoryService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.api.JWTTokenInterceptor;
import io.hobaskos.event.eventapp.data.api.LocationService;
import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.repository.LocationRepository;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
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
    public EventService.Authenticated providesEventServiceAuthenticated(Cache cache, JWTTokenInterceptor interceptor) {
        return ApiService.build(httpUrl).createService(EventService.Authenticated.class, cache, interceptor);
    }

    @Singleton
    @Provides
    public UserService providesUserJWTService(Cache cache, JWTTokenInterceptor interceptor) {
        return ApiService.build(httpUrl).createService(UserService.class, cache, interceptor);
    }

    @Singleton
    @Provides
    public UserRepository providesUserRepository(UserService service, JwtStorageProxy storage, PersistentStorage persistentStorage){
        return new UserRepository(service, storage, persistentStorage);
    }

    @Singleton
    @Provides
    public EventRepository providesEventRepository(EventService.Anonymously eventServiceAnonymously, EventService.Authenticated eventServiceAuthenticated) {
        return new EventRepository(eventServiceAnonymously, eventServiceAuthenticated);
    }

    @Singleton
    @Provides
    public JWTTokenInterceptor providesIntercepter(JwtStorageProxy storageProxy) {
        return new JWTTokenInterceptor(storageProxy);
    }

    @Singleton
    @Provides
    public EventCategoryService providesEventCategoryService(Cache cache) {
        return ApiService.build(httpUrl).createService(EventCategoryService.class, cache);
    }

    @Singleton
    @Provides
    public EventCategoryRepository providesEventCategoryRepository(EventCategoryService eventCategoryService) {
        return new EventCategoryRepository(eventCategoryService);
    }

    @Singleton
    @Provides
    public LocationService providesLocationService(Cache cache, JWTTokenInterceptor interceptor) {
        return ApiService.build(httpUrl).createService(LocationService.class, cache, interceptor);
    }

    @Singleton
    @Provides
    public LocationRepository providesLocationRepository(LocationService service){ return new LocationRepository(service); }

}

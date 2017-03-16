package io.hobaskos.event.eventapp.module;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.api.AccountService;
import io.hobaskos.event.eventapp.data.api.ApiService;
import io.hobaskos.event.eventapp.data.api.EventCategoryService;
import io.hobaskos.event.eventapp.data.api.EventService;
import io.hobaskos.event.eventapp.data.api.JWTTokenInterceptor;
import io.hobaskos.event.eventapp.data.api.UserService;
import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.repository.AccountRepository;
import io.hobaskos.event.eventapp.data.repository.UserRepository;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
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
    public EventService.Authenticated providesEventServiceAuthenticated(Cache cache, JWTTokenInterceptor intercepter) {
        return ApiService.build(httpUrl).createService(EventService.Authenticated.class, cache, intercepter);
    }

    @Singleton
    @Provides
    public UserService providesUserJWTService(Cache cache, JWTTokenInterceptor intercepter) {
        return ApiService.build(httpUrl).createService(UserService.class, cache, intercepter);
    }

    @Singleton
    @Provides
    public UserRepository providesUserRepository(UserService service, JwtStorageProxy storage){ return new UserRepository(service, storage); }

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
    public AccountService providesAccountService(Cache cache) {
        return ApiService.build(httpUrl).createService(AccountService.class, cache);
    }

    @Singleton
    @Provides
    public AccountRepository providesAccountRepository(AccountService accountService) {
        return new AccountRepository(accountService);
    }



}

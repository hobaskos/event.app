package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.UserManager;
import io.hobaskos.event.eventapp.data.storage.JwtStorageProxy;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;

/**
 * Created by alex on 1/26/17.
 */
@Module
public class AppModule {

    public App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public App providesApp() {
        return app;
    }

    @Provides
    @Singleton
    public PersistentStorage providesStorage(App app){
        return new PersistentStorage(app);
    }

    @Provides
    @Singleton
    public UserManager providesUserManager(JwtStorageProxy jwtStorageProxy)
    {
        return new UserManager(jwtStorageProxy);
    }
}

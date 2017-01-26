package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.hobaskos.event.eventapp.App;

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
}

package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.ui.event.EventPresenter;
import io.hobaskos.event.eventapp.ui.events.EventsPresenter;

/**
 * Created by alex on 1/26/17.
 */
@Singleton
@Component(modules = {AppModule.class,
                      NetModule.class})
public interface DiComponent {

    void inject(EventsPresenter eventsPresenter);
    void inject(EventPresenter eventPresenter);

}

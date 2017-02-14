package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.ui.event.EventActivity;
import io.hobaskos.event.eventapp.ui.events.old.EventsActivity;
import io.hobaskos.event.eventapp.ui.events.old.EventsFragment;

/**
 * Created by alex on 1/26/17.
 */
@Singleton
@Component(modules = {AppModule.class,
                      NetModule.class})
public interface DiComponent {

    void inject(EventsActivity eventsActivity);
    void inject(EventsFragment eventsFragment);
    void inject(io.hobaskos.event.eventapp.ui.events.EventsFragment eventsFragment);
    void inject(EventActivity eventActivity);

}

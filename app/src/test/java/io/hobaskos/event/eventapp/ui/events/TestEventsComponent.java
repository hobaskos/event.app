package io.hobaskos.event.eventapp.ui.events;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.module.TestNetModule;

/**
 * Created by andre on 2/15/2017.
 */

@Singleton
@Component(
        modules = { TestNetModule.class })
public interface TestEventsComponent {

    public EventsPresenter presenter();

    public void inject(EventsPresenterTest1 test);
}

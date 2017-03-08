package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventActivity;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.list.EventsFragment;
import io.hobaskos.event.eventapp.ui.main.MainActivity;
import io.hobaskos.event.eventapp.ui.profile.ProfileActivity;


/**
 * Created by alex on 1/26/17.
 */
@Singleton
@Component(modules = {AppModule.class,
                      NetModule.class})
public interface DiComponent {
    void inject(EventActivity eventActivity);
    void inject(EventsFragment eventsFragment);
    void inject(CreateEventActivity createEventActivity);
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(ProfileActivity profileActivity);

}

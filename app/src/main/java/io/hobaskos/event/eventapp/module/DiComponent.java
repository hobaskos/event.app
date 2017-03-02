package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;
import io.hobaskos.event.eventapp.ui.event.EventActivity;
import io.hobaskos.event.eventapp.ui.events.EventsFragment;
import io.hobaskos.event.eventapp.ui.login.LoginPresenter;
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
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(ProfileActivity profileActivity);
}

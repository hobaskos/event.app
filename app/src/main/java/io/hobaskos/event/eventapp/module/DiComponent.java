package io.hobaskos.event.eventapp.module;

import javax.inject.Singleton;

import dagger.Component;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventActivity;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventFragment;
import io.hobaskos.event.eventapp.ui.event.create.CreateEventPresenter;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsFragment2;
import io.hobaskos.event.eventapp.ui.location.add.LocationActivity;
import io.hobaskos.event.eventapp.ui.event.details.EventActivity;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsFragment;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsFragment;
import io.hobaskos.event.eventapp.ui.event.search.map.SearchEventsMapFragment;
import io.hobaskos.event.eventapp.ui.login.LoginActivity;
import io.hobaskos.event.eventapp.ui.login.LoginPresenter;
import io.hobaskos.event.eventapp.ui.main.old.MainActivity;
import io.hobaskos.event.eventapp.ui.profile.ProfileActivity;
import io.hobaskos.event.eventapp.ui.profile.ProfileEditActivity;


/**
 * Created by alex on 1/26/17.
 */
@Singleton
@Component(modules = {AppModule.class,
                      NetModule.class})
public interface DiComponent {
    void inject(EventActivity eventActivity);
    void inject(LocationActivity locationActivity);
    void inject(EventsFragment eventsFragment);
    void inject(EventsFragment2 eventsFragment);
    void inject(SearchEventsMapFragment eventsMapFragment);
    void inject(CreateEventActivity createEventActivity);
    void inject(CreateEventFragment createEventFragment);
    void inject(CreateEventPresenter createEventPresenter);
    void inject(MainActivity mainActivity);
    void inject(io.hobaskos.event.eventapp.ui.main.MainActivity mainActivity);
    void inject(FilterEventsFragment filterEventsFragment);
    void inject(LoginPresenter loginPresenter);
    void inject(LoginActivity loginActivity);
    void inject(ProfileActivity profileActivity);
    void inject(ProfileEditActivity profileEditActivity);
}

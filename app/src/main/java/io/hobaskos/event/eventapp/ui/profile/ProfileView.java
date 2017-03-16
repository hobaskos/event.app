package io.hobaskos.event.eventapp.ui.profile;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.User;

/**
 * Created by Magnus on 22.02.2017.
 */

public interface ProfileView extends MvpView{
    void setProfileData(User user);
    void setEventAttending(List<Event> eventAttending);
}

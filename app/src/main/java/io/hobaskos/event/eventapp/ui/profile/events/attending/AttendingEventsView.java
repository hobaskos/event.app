package io.hobaskos.event.eventapp.ui.profile.events.attending;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by Magnus on 16.03.2017.
 */

public interface AttendingEventsView extends MvpView {
    void setEventAttending(List<Event> eventAttending);
}
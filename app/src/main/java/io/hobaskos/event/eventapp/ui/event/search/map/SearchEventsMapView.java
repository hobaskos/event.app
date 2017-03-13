package io.hobaskos.event.eventapp.ui.event.search.map;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by test on 3/13/2017.
 */

public interface SearchEventsMapView extends MvpView {
    void setEvents(List<Event> events);
}

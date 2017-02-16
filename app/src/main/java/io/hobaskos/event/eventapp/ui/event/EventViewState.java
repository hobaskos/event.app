package io.hobaskos.event.eventapp.ui.event;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by Magnus on 16.02.2017.
 */

public class EventViewState implements LceViewState<Event, EventView> {
    @Override
    public void setStateShowContent(Event loadedData) {

    }

    @Override
    public void setStateShowError(Throwable e, boolean pullToRefresh) {

    }

    @Override
    public void setStateShowLoading(boolean pullToRefresh) {

    }

    @Override
    public void apply(EventView view, boolean retained) {

    }
}

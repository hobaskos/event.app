package io.hobaskos.event.eventapp.ui.event;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 1/26/2017.
 */

public interface EventView {
    void showWait();

    void removeWait();

    void onFailure(String errorMessage);

    void setEvent(Event event);
}

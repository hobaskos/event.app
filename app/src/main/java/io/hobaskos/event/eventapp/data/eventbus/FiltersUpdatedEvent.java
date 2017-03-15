package io.hobaskos.event.eventapp.data.eventbus;

/**
 * Created by test on 3/15/2017.
 */

public class FiltersUpdatedEvent {

    String message;

    public FiltersUpdatedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

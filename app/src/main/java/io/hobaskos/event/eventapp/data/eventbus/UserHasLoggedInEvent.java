package io.hobaskos.event.eventapp.data.eventbus;

import io.hobaskos.event.eventapp.data.model.User;

/**
 * Created by hans on 22/03/2017.
 */

public class UserHasLoggedInEvent {

    private final User user;

    public UserHasLoggedInEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

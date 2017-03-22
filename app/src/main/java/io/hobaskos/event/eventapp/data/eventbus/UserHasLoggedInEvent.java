package io.hobaskos.event.eventapp.data.eventbus;

/**
 * Created by hans on 22/03/2017.
 */

public class UserHasLoggedInEvent {

    private final String name;
    private final String imageUrl;

    public UserHasLoggedInEvent(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}

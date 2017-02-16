package io.hobaskos.event.eventapp.data.model;

/**
 * Created by alex on 2/15/17.
 */

public class EventCategory {

    private Long id;

    private String title;

    private String iconUrl;

    private EventCategoryTheme theme;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public EventCategoryTheme getTheme() {
        return theme;
    }
}

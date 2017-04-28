package io.hobaskos.event.eventapp.data.eventbus;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Location;

/**
 * Created by alex on 4/28/17.
 */

public class EventHasUpdatedLocations {

    private List<Location> locations;

    public EventHasUpdatedLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }
}

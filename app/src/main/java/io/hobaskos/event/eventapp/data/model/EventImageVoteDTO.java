package io.hobaskos.event.eventapp.data.model;

/**
 * Created by hans on 24/03/2017.
 */

public class EventImageVoteDTO {

    private Long eventImageId;
    private int vote;

    public Long getEventImageId() {
        return eventImageId;
    }

    public void setEventImageId(Long eventImageId) {
        this.eventImageId = eventImageId;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}

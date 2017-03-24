package io.hobaskos.event.eventapp.data.repository;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventImageVoteDTO;
import rx.Observable;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.EventImageVoteService;

/**
 * Created by hans on 24/03/2017.
 */

public class EventImageVoteRepository {

    private EventImageVoteService service;

    @Inject
    public EventImageVoteRepository(EventImageVoteService service) {
        this.service = service;
    }

    public Observable<EventImageVoteDTO> postVote(EventImageVoteDTO eventImageVoteDTO) {
        return service.postVote(eventImageVoteDTO);
    }

    public Observable<Void> deleteVote(Long id) {
        return service.deleteVote(id);
    }

}

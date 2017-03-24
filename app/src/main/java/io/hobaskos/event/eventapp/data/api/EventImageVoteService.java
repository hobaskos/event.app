package io.hobaskos.event.eventapp.data.api;

import rx.Observable;

import io.hobaskos.event.eventapp.data.model.EventImageVoteDTO;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hans on 24/03/2017.
 */

public interface EventImageVoteService {

    @DELETE("api/event-image-votes/{id}")
    Observable<Void> deleteVote(@Path("id") Long id);

    @POST("api/event-image-votes")
    Observable<EventImageVoteDTO> postVote(@Body EventImageVoteDTO eventImageDTO);


}

package io.hobaskos.event.eventapp.data.api;


import java.util.List;

import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hans on 27/03/2017.
 */

public interface EventImageService {

    @GET("api/event-polls/{id}/event-images")
    Observable<List<CompetitionImage>> getCompetitionImages(@Path("id") Long id);

    @POST("api/event-images")
    Observable<CompetitionImage> saveImage(@Body CompetitionImage competitionImage);

}

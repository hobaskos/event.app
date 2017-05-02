package io.hobaskos.event.eventapp.data.api;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.EventCategory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by alex on 2/15/17.
 */

public interface EventCategoryService {

    @GET("api/event-categories")
    Observable<List<EventCategory>> getEventCategories(@Query("page") int page,
                                                       @Query("size") int pageSize);

    @GET("api/event-categories/{id}")
    Observable<EventCategory> getEventCategory(@Path("id") Long id);
}

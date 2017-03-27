package io.hobaskos.event.eventapp.data.repository;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.api.EventImageService;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import rx.Observable;

/**
 * Created by hans on 27/03/2017.
 */

public class EventImageRepository {

    private EventImageService service;

    @Inject
    public EventImageRepository(EventImageService service) {
        this.service = service;
    }

    public Observable<List<CompetitionImage>> getCompetitionImages(Long id) {
        return service.getCompetitionImages(id);
    }

    public Observable<CompetitionImage> saveImage(CompetitionImage competitionImage) {
        return service.saveImage(competitionImage);
    }

}

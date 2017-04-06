package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.CompetitionImage;

/**
 * Created by hans on 23/03/2017.
 */

public interface ImageCarouselView extends MvpView {

    void voteWasSuccessful();
    void voteWasUnsuccessful();
    void setData(List<CompetitionImage> data);

}

package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import io.hobaskos.event.eventapp.data.model.CompetitionImage;

/**
 * Created by hans on 24/03/2017.
 */

public interface OnCompetitionListInteractionListener {
    void onCompetitionImageClick(CompetitionImage competitionImage);
    void onCompetitionVoteButtonClicked(CompetitionImage competitionImage, int vote);
}

package io.hobaskos.event.eventapp.ui.event.details.competition.list;

/**
 * Created by hans on 24/03/2017.
 */

public interface OnCompetitionListInteractionListener {
    void onCompetitionImageClick(Long id);
    void onCompetitionVoteButtonClicked(Long id, int vote);
}

package io.hobaskos.event.eventapp.ui.event.details.competition.list;

/**
 * Created by hans on 24/03/2017.
 */

public interface OnCompetitionListInteractionListener {
    void onListFragmentInteraction(Long id);
    void onCompetitionImageVoteSubmitted(Long id, int vote);
}

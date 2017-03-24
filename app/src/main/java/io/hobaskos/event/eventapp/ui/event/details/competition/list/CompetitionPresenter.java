package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.repository.EventImageVoteRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;

/**
 * Created by hans on 24/03/2017.
 */

public class CompetitionPresenter extends BaseRxLcePresenter<CompetitionView,List<CompetitionImage>> {

    public static final String IMAGE_ONE = "http://cdn01.androidauthority.net/wp-content/uploads/2016/06/draw-300x170.png";
    public static final String IMAGE_TWO = "http://cdn01.androidauthority.net/wp-content/uploads/2016/06/android-win-2-300x162.png";
    public static final String IMAGE_THREE = "http://cdn01.androidauthority.net/wp-content/uploads/2016/06/android-win-1-300x214.png";
    public static final String TAG = CompetitionPresenter.class.getName();

    @State
    protected Long eventId;

    private static final String Apple = "";

    private EventImageVoteRepository eventImageVoteRepository;

    @Inject
    public CompetitionPresenter(EventImageVoteRepository eventImageVoteRepository) {
        this.eventImageVoteRepository = eventImageVoteRepository;
    }

    public void get(Long id) {
        Log.i(TAG, "get(" + id + ")");
        if(isViewAttached() && getView() != null) {
            Log.i(TAG, "view is attached");
            getView().setData(getCompetitionImageList());
        }
    }

    private ArrayList<CompetitionImage> getCompetitionImageList() {

        CompetitionImage ci1 = new CompetitionImage();
        ci1.setId(1L);
        ci1.setImageUrl(IMAGE_ONE);
        ci1.setNumberOfVotes(20);
        ci1.setHasMyVote(true);
        ci1.setOwnerLogin("Hans");

        CompetitionImage ci2 = new CompetitionImage();
        ci2.setId(2L);
        ci2.setImageUrl(IMAGE_TWO);
        ci2.setNumberOfVotes(10);
        ci2.setHasMyVote(true);
        ci2.setOwnerLogin("Magnus");

        CompetitionImage ci3 = new CompetitionImage();
        ci3.setId(3L);
        ci3.setImageUrl(IMAGE_THREE);
        ci3.setNumberOfVotes(30);
        ci3.setHasMyVote(false);
        ci3.setOwnerLogin("Alex");

        ArrayList<CompetitionImage> competitionItems = new ArrayList<>();
        competitionItems.add(ci1);
        competitionItems.add(ci2);
        competitionItems.add(ci3);

        return competitionItems;
    }
}

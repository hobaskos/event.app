package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.EventImageVoteDTO;
import io.hobaskos.event.eventapp.data.repository.EventImageRepository;
import io.hobaskos.event.eventapp.data.repository.EventImageVoteRepository;
import io.hobaskos.event.eventapp.ui.event.details.competition.list.CompetitionPresenter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hans on 23/03/2017.
 */

public class ImageCarouselPresenter extends MvpBasePresenter<ImageCarouselView> {

    public static final String TAG = ImageCarouselPresenter.class.getName();

    private EventImageVoteRepository eventImageVoteRepository;
    private EventImageRepository eventImageRepository;

    @State
    protected Long competitionId;

    @Inject
    public ImageCarouselPresenter(EventImageVoteRepository eventImageVoteRepository,
                                  EventImageRepository eventImageRepository) {

        this.eventImageVoteRepository = eventImageVoteRepository;
        this.eventImageRepository = eventImageRepository;

    }

    public void setCompetitionId(Long id) {
        this.competitionId = id;
    }

    public void get() {
        if(competitionId != null) {
            Log.i(TAG, "get() with competitionId == " + competitionId);
            eventImageRepository
                    .getCompetitionImages(competitionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<CompetitionImage>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "Could not fetch list of competition images.");
                            Log.i(TAG, e.getMessage());
                        }

                        @Override
                        public void onNext(List<CompetitionImage> competitionImages) {
                            Log.i(TAG, "Could fetch list of competition images.");
                            Log.i(TAG, "Size=" + competitionImages.size());
                            for(int i = 0; i < competitionImages.size(); i++) {
                                Log.i(TAG, "ImageUrl= " + competitionImages.get(i).getAbsoluteImageUrl());
                            }
                            if(isViewAttached() && getView() != null) {
                                getView().setData(competitionImages);
                            }
                        }
                    });
        }
    }

    public void vote(Long id, int vote) {
        Log.i(TAG, "Voting: " + vote);
        EventImageVoteDTO eventImageVoteDTO = new EventImageVoteDTO();
        eventImageVoteDTO.setEventImageId(id);
        eventImageVoteDTO.setVote(vote);
        eventImageVoteRepository
                .postVote(eventImageVoteDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventImageVoteDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "Vote unsuccessful");
                    }

                    @Override
                    public void onNext(EventImageVoteDTO eventImageVoteDTO) {
                        Log.i(TAG, "Vote successful");
                        get();
                    }
                });
    }

}

package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.CompetitionImage;
import io.hobaskos.event.eventapp.data.model.EventImageVoteDTO;
import io.hobaskos.event.eventapp.data.repository.EventImageRepository;
import io.hobaskos.event.eventapp.data.repository.EventImageVoteRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hans on 24/03/2017.
 */

public class CompetitionPresenter extends MvpBasePresenter<CompetitionView> {

    public static final String TAG = CompetitionPresenter.class.getName();

    @State
    protected Long eventId;
    @State
    protected Long competitionId;

    private EventImageVoteRepository eventImageVoteRepository;
    private EventImageRepository eventImageRepository;

    @Inject
    public CompetitionPresenter(EventImageVoteRepository eventImageVoteRepository, EventImageRepository eventImageRepository) {
        this.eventImageVoteRepository = eventImageVoteRepository;
        this.eventImageRepository = eventImageRepository;
    }

    public void setCompetitionId(Long id) {
        competitionId = id;
    }

    public void get() {

        if(competitionId != null) {

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
                    }
                });
    }

    public void nominateImage(String title, String image) {
        Log.i(TAG, "Nominating image...");
        if(competitionId != null) {
            CompetitionImage competitionImage = new CompetitionImage();
            competitionImage.setTitle(title);
            competitionImage.setCompetitionId(competitionId);
            competitionImage.setImageBase64(image);
            competitionImage.setFileContentType("image/jpg");

            eventImageRepository.saveImage(competitionImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CompetitionImage>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i(TAG, "Could not save competition image.");
                            Log.i(TAG, "Error: " + e.getMessage());
                            if(isViewAttached() && getView() != null) {
                                getView().imageWasUnsuccessfullyNominated(e);
                            }
                        }

                        @Override
                        public void onNext(CompetitionImage competitionImage) {
                            Log.i(TAG, "Competition image saved successfully.");
                            if(isViewAttached() && getView() != null) {
                                getView().imageWasSuccessfullyNominated(competitionImage);
                            }
                        }
                    });
        }
    }
}

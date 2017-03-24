package io.hobaskos.event.eventapp.ui.event.details.competition.carousel;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.EventImageVoteDTO;
import io.hobaskos.event.eventapp.data.repository.EventImageVoteRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hans on 23/03/2017.
 */

public class ImageCarouselPresenter implements MvpPresenter<ImageCarouselView> {


    private ImageCarouselView view;
    private EventImageVoteRepository repository;

    @Inject
    public ImageCarouselPresenter(EventImageVoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(ImageCarouselView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        // do something..
    }

    public void vote(Long id) {
        EventImageVoteDTO eventImageVoteDTO = new EventImageVoteDTO();
        eventImageVoteDTO.setEventImageId(id);
        eventImageVoteDTO.setVote(1);
        repository.postVote(eventImageVoteDTO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EventImageVoteDTO>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view != null) {
                            view.voteWasUnsuccessful();
                        }
                    }

                    @Override
                    public void onNext(EventImageVoteDTO eventImageVoteDTO) {
                        if(view != null) {
                            view.voteWasSuccessful();
                        }
                    }
                });
    }

    public void delete(Long id) {
        repository.deleteVote(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view != null) {
                            view.voteWasUnsuccessful();
                        }
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        if(view != null) {
                            view.voteWasSuccessful();
                        }
                    }
                });
    }

}

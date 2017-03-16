package io.hobaskos.event.eventapp.ui.event.details.attending;

import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 3/15/17.
 */

public class AttendeesPresenter extends BaseRxLcePresenter<AttendeesView, List<User>> {

    private EventRepository eventRepository;

    private Subscriber<List<User>> moreAttendeesObserver;

    @State
    protected int page = 0;

    @State
    protected Long eventId;

    @Inject
    public AttendeesPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void loadAttendees(boolean pullToRefresh, Long eventId) {

        this.eventId = eventId;

        if (isViewAttached()) { getView().showLoadMore(false); }

        subscribe(eventRepository.getAddendingUsers(eventId, page), pullToRefresh);
    }

    public void loadMoreAttendees() {

        if (isViewAttached()) { getView().showLoadMore(false); }
        unsubscribe();

        if (moreAttendeesObserver != null && !moreAttendeesObserver.isUnsubscribed()) {
            moreAttendeesObserver.unsubscribe();
        }

        moreAttendeesObserver = new Subscriber<List<User>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                getView().showLoadMoreError(e);
                getView().showLoadMore(false);
            }

            @Override
            public void onNext(List<User> users) {
                if (isViewAttached()) {
                    getView().addMoreData(users);
                    getView().showLoadMore(false);
                }
            }
        };

        eventRepository.getAddendingUsers(eventId, ++page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moreAttendeesObserver);
    }
}

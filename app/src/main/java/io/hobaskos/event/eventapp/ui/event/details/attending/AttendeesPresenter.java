package io.hobaskos.event.eventapp.ui.event.details.attending;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import icepick.State;
import io.hobaskos.event.eventapp.data.model.EventAttendance;
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

    public static final String TAG = AttendeesPresenter.class.getName();

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

        Log.d(TAG, "loadAttendees: " + eventId);

        this.eventId = eventId;

        if (isViewAttached()) { getView().showLoadMore(false); }
        page = 0;
        subscribe(eventRepository.getAddendingUsers(eventId, page), pullToRefresh);
    }

    public void loadMoreAttendees() {

        Log.d(TAG, "loadMoreAttendees: " + eventId);

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

    public void attendEvent(Observer<EventAttendance> observer) {
        Log.d(TAG, "attendEvent: " + eventId);
        eventRepository.attendEvent(eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

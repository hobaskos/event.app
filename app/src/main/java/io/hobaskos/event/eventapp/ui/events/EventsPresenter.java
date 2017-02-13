package io.hobaskos.event.eventapp.ui.events;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsPresenter extends BaseRxLcePresenter<EventsView, List<Event>> {

    protected EventRepository eventRepository;

    private int queryLimit = 20;
    private Subscriber<List<Event>> moreEventSubscriber;

    @Inject
    public EventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void loadEvents(boolean pullToRefresh) {
        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }

        subscribe(eventRepository.getAll(0), pullToRefresh);
    }

    public void loadMoreEvents(int nextPage) {
        // Cancel any previous query
        unsubscribe();

        final Observable<List<Event>> observable = eventRepository.getAll(nextPage);

        if (isViewAttached()) {
            getView().showLoadMore(true);
        }

        moreEventSubscriber = new Subscriber<List<Event>>() {
            @Override public void onCompleted() {
            }

            @Override  public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }

            @Override public void onNext(List<Event> events) {
                if (isViewAttached()) {
                    getView().addMoreData(events);
                    getView().showLoadMore(false);
                }
            }
        };

        // start
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moreEventSubscriber);

    }

    @Override protected void unsubscribe() {
        super.unsubscribe();
        if (moreEventSubscriber != null && !moreEventSubscriber.isUnsubscribed()) {
            moreEventSubscriber.unsubscribe();
        }
    }


}

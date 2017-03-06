package io.hobaskos.event.eventapp.ui.event.list;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsPresenter extends BaseRxLcePresenter<EventsView, List<EventsPresentationModel>> {

    protected EventRepository eventRepository;

    private Subscriber<List<EventsPresentationModel>> moreEventSubscriber;

    private Func1<List<Event>, List<EventsPresentationModel>> presentationModelTransformation;


    @Inject
    public EventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        presentationModelTransformation = events -> {
            List<EventsPresentationModel> pmEvents = new ArrayList<>();
            for (Event event : events) {
                EventsPresentationModel pm = new EventsPresentationModel(event);
                pmEvents.add(pm);
            }
            return pmEvents;
        };
    }

    public void loadEvents(boolean pullToRefresh) {
        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }

        final Observable<List<EventsPresentationModel>> observable =
                eventRepository.getAll(0).map(presentationModelTransformation);

        subscribe(observable, pullToRefresh);
    }

    public void loadMoreEvents(int nextPage) {
        // Cancel any previous query
        unsubscribe();

        final Observable<List<EventsPresentationModel>> observable =
                eventRepository.getAll(nextPage).map(presentationModelTransformation);

        if (isViewAttached()) {
            getView().showLoadMore(true);
        }

        moreEventSubscriber = new Subscriber<List<EventsPresentationModel>>() {
            @Override public void onCompleted() {
            }

            @Override  public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }

            @Override public void onNext(List<EventsPresentationModel> events) {
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

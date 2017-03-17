package io.hobaskos.event.eventapp.ui.profile.events.mine;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.eventbus.SetEventsEvent;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.data.storage.PersistentStorage;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLcePresenter;
import io.hobaskos.event.eventapp.ui.event.filter.FilterEventsPresenter;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsPresenter;
import io.hobaskos.event.eventapp.ui.event.search.list.EventsView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by test on 3/16/2017.
 */

public class MyEventsPresenter extends BaseRxLcePresenter<MyEventsView, List<Event>> {

    public final static String TAG = MyEventsPresenter.class.getName();

    protected EventRepository eventRepository;

    private Subscriber<List<Event>> moreEventSubscriber;


    @Inject
    public MyEventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void loadEvents(boolean pullToRefresh) {

        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }

        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.getMyEvents(0);

        // setup and start subscription:
        subscribe(observable, pullToRefresh);
    }

    public void loadMoreEvents(int nextPage) {
        // Cancel any previous query
        unsubscribe();

        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.getMyEvents(nextPage);
        // Show loading in view:
        if (isViewAttached()) {
            getView().showLoadMore(true);
        }
        // Setup subscriber:
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
        // start subscription:
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

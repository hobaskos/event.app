package io.hobaskos.event.eventapp.ui.profile.events.archived;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.presenter.BaseRxLoadMoreLcePresenter;
import io.hobaskos.event.eventapp.ui.profile.events.attending.AttendingEventsView;
import io.hobaskos.event.eventapp.ui.profile.events.mine.MyEventsPresenter;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by test on 3/19/2017.
 */

public class ArchivedEventsPresenter extends BaseRxLoadMoreLcePresenter<ArchivedEventsView, List<Event>> {

    public final static String TAG = MyEventsPresenter.class.getName();

    protected EventRepository eventRepository;

    private Subscriber<List<Event>> moreEventSubscriber;


    @Inject
    public ArchivedEventsPresenter(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void loadEvents(boolean pullToRefresh) {

        // in case the previous action was load more we have to reset the view
        if (isViewAttached()) {
            getView().showLoadMore(false);
        }
        // TODO: Change repo call from getAttending to getArchived !!


        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.getAttendingEvents(0);

        // setup and start subscription:
        subscribe(observable, pullToRefresh);
    }

    public void loadMoreEvents(int nextPage) {
        // Cancel any previous query
        unsubscribe();
        // TODO: Change repo call from getAttending to getArchived !!
        // Setup observable:
        final Observable<List<Event>> observable =
                eventRepository.getAttendingEvents(nextPage);
        // Show loading in view:
        if (isViewAttached()) {
            getView().showLoadMore(true);
        }

        subscribeMore(observable);
    }

    @Override protected void unsubscribe() {
        super.unsubscribe();
        if (moreEventSubscriber != null && !moreEventSubscriber.isUnsubscribed()) {
            moreEventSubscriber.unsubscribe();
        }
    }
}
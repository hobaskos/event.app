package io.hobaskos.event.eventapp.ui.event;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.BasePresenter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andre on 1/26/2017.
 */
public class EventPresenter extends BasePresenter<EventView> {

    private EventRepository eventRepository;
    private Observable<Event> eventObservable;

    //private CompositeSubscription subscriptions;

    @Inject
    public EventPresenter(EventRepository eventRepository) {

        this.eventRepository = eventRepository;

        //this.subscriptions = new CompositeSubscription();
    }

    public void getEvent(Long id) {
        getView().showLoading();

        eventObservable = eventRepository.get(id)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());

        eventObservable.subscribe(
                    event -> getView().setData(event),
                    throwable -> getView().showError(throwable.getMessage())
                );

        //subscriptions.add(subscription);
    }

    public Observable<Event> getObservable() {
        return eventObservable;
    }

    public void onStop() {
        //subscriptions.unsubscribe();
    }
}


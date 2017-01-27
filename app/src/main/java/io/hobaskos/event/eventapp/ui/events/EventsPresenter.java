package io.hobaskos.event.eventapp.ui.events;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsPresenter {

    @Inject
    public EventRepository repository;

    private final EventsView view;
    private CompositeSubscription subscriptions;

    public EventsPresenter(EventsView view) {

        App.getInst().getDiComponent().inject(this);

        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getEvents() {
        view.showWait();

        repository.getAll().subscribeOn(Schedulers.io())
                .doOnNext((list) -> {
                    view.setEvents(list);
                }).doOnError((throwable) -> {
                    view.onFailure(throwable.getMessage());
                });


        //subscriptions.add(subscription);
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }
}

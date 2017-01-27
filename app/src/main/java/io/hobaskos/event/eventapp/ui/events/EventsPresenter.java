package io.hobaskos.event.eventapp.ui.events;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.App;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import io.hobaskos.event.eventapp.ui.base.BasePresenter;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andre on 1/26/2017.
 */
public class EventsPresenter extends BasePresenter<EventsView> {

    @Inject
    public EventRepository repository;

    private CompositeSubscription subscriptions;

    public EventsPresenter() {

        App.getInst().getDiComponent().inject(this);

        this.subscriptions = new CompositeSubscription();
    }

    public void getEvents() {
        getView().showLoading();

        repository.getAll().subscribeOn(Schedulers.io())
                .doOnNext((list) -> {
                    getView().setData(list);
                }).doOnError((throwable) -> {
                    getView().showError(throwable.getMessage());
                });


        //subscriptions.add(subscription);
    }
    public void onStop() {
        subscriptions.unsubscribe();
    }
}

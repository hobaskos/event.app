package io.hobaskos.event.eventapp.ui.event.create;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 11.03.2017.
 */

public class CreateEventPresenter extends MvpBasePresenter<CreateEventView> {

    public EventRepository eventRepository;
    public EventCategoryRepository eventCategoryRepository;

    @Inject
    public CreateEventPresenter(EventRepository eRepository, EventCategoryRepository ecRepository) {
        this.eventRepository = eRepository;
        this.eventCategoryRepository = ecRepository;
    }

    protected void post(Event event) {

        Log.i("CreateEventPresenter", event.toString());
        eventRepository.save(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Event>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Presenter. Error=", e.getMessage());
                        if(isViewAttached()){
                            getView().onFailure();
                        }
                    }

                    @Override
                    public void onNext(Event event) {
                        if(isViewAttached()){
                            getView().onSuccess(event);
                        }
                    }
                });
    }

    protected void loadCategories() {

        eventCategoryRepository.getAll(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EventCategory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached()) {
                            // Todo: rewrite onFailure to handle every failure -> take a string
                            getView().onFailureLoadingCategories();
                        }
                    }

                    @Override
                    public void onNext(List<EventCategory> eventCategoryList) {
                        if(isViewAttached()){
                            getView().onCategoriesLoaded(eventCategoryList);
                        }
                    }
                });
    }

    protected void update(Event event) {

        Log.i("CreateEventPresenter", event.toString());
        eventRepository.update(event)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Event>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("CreateEventPresenter", e.getMessage());
                e.printStackTrace();
                if(isViewAttached()) {
                    getView().onFailure();
                }
            }

            @Override
            public void onNext(Event event) {
                if(isViewAttached()) {
                    getView().onSuccess(event);
                }
            }
        });

    }

}

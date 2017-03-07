package io.hobaskos.event.eventapp.ui.event.create;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.AccountManager;
import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.data.model.EventCategory;
import io.hobaskos.event.eventapp.data.model.User;
import io.hobaskos.event.eventapp.data.repository.EventCategoryRepository;
import io.hobaskos.event.eventapp.data.repository.EventRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hansp on 07.03.2017.
 */

public class CreateEventPresenter extends MvpBasePresenter<CreateEventView> {

    private static final String TAG = "CreateEventPresenter";

    public EventRepository repository;
    public EventCategoryRepository eventCategoryRepository;
    public AccountManager accountManager;

    @Inject
    public CreateEventPresenter(EventRepository eventRepository,
                                EventCategoryRepository eventCategoryRepository,
                                AccountManager accountManager)
    {
        this.repository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.accountManager = accountManager;

        // fetch Account information and store it inside AccountManager.User
        //this.accountManager.refresh();
    }

    protected void getEventCategories()
    {
        Log.i(TAG, "fetchEventCategories()");
        eventCategoryRepository.getAll(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EventCategory>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "did not find categories");
                        Log.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(List<EventCategory> categories) {
                        Log.i(TAG, "found categories");
                        Log.i(TAG, Integer.toString(categories.size()));
                        if(isViewAttached())
                        {
                            Log.i(TAG, "View is attached!");
                            getView().onCategoriesLoaded(categories);
                        }
                    }
                });

    }

    protected void create(Event event)
    {
        if(!accountManager.isLoggedIn())
        {
            // bla bla
            return;
        }

        // check if Account information / username exists


    }


}

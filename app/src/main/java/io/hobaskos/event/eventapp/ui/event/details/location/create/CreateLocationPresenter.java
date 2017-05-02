package io.hobaskos.event.eventapp.ui.event.details.location.create;

import android.util.Log;

import com.google.gson.JsonPrimitive;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Location;
import io.hobaskos.event.eventapp.data.repository.LocationRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by osvold.hans.petter on 13.03.2017.
 */

public class CreateLocationPresenter implements MvpPresenter<CreateLocationView> {

    private CreateLocationView view;
    private LocationRepository repository;

    @Inject
    public CreateLocationPresenter(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(CreateLocationView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if(!retainInstance) {
            this.view = null;
        }
    }

    public void addLocation(Location location) {
        Log.i("LocationPresenter", location.toString());
        Log.i("LocationPresenter", "Event-id:" + location.getEventId());

        JsonPrimitive jsonPrimitive = new JsonPrimitive(location.getFromDate().toString());
        Log.i("LocationPresenter", "jsonPrimitive=" + jsonPrimitive);

        repository.save(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("LocationPresenter", e.getMessage());
                e.printStackTrace();
                if(view != null) {
                    view.onFailure();
                }
            }

            @Override
            public void onNext(Location location) {
                if(view != null) {
                    view.onSuccess();
                }
            }
        });
    }

    public void updateLocation(Location location) {

        repository.put(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Location>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(view != null) {
                    view.onFailure();
                }
            }

            @Override
            public void onNext(Location location) {
                if(view != null) {
                    view.onSuccess();
                }
            }
        });
    }
}

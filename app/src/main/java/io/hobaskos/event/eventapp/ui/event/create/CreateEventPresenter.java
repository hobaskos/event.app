package io.hobaskos.event.eventapp.ui.event.create;

import android.os.Handler;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by hansp on 11.03.2017.
 */

public class CreateEventPresenter extends MvpBasePresenter<CreateEventView> {

    @Inject
    public CreateEventPresenter() {

    }

    protected void post(Event event) {

    }

    protected void loadCategories() {

        List<String> categories = new ArrayList<>();
        categories.add("Category");
        categories.add("Music");
        categories.add("Sports");
        categories.add("Nightlife");
        categories.add("Art");

        // Just to simulate IO
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(isViewAttached()){
                getView().onCategoriesLoaded(categories);
            }
        }, 2000);

    }

    protected void loadThemes() {
        List<String> themes = new ArrayList<>();
        themes.add("Theme");
        themes.add("Blue");
        themes.add("Purple");
        themes.add("Yellow");
        themes.add("Red");

        // Just to simulate IO
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if(isViewAttached()){
                getView().onThemesLoaded(themes);
            }
        }, 2000);
    }

}

package io.hobaskos.event.eventapp.ui.event.create;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by hansp on 11.03.2017.
 */

public interface CreateEventView extends MvpView {
    void onSuccess(int id);
    void onFailure();
    void onCategoriesLoaded(List<String> categoryList);
    void onThemesLoaded(List<String> themesList);
}

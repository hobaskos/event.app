package io.hobaskos.event.eventapp.ui.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

import io.hobaskos.event.eventapp.data.model.User;

/**
 * Created by hansp on 25.02.2017.
 */

public interface MainView extends MvpView {

    void setNavigationHeaderText(String text);
    void viewAuthenticatedNavigation();
    void viewAnonymousNavigation();
    void hideNavigationHeader();
}

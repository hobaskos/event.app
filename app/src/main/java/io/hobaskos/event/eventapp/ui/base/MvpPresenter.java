package io.hobaskos.event.eventapp.ui.base;

/**
 * Created by andre on 2/2/2017.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttachView(V view);

}

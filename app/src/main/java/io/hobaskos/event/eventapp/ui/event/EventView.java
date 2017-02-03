package io.hobaskos.event.eventapp.ui.event;

import android.support.annotation.UiThread;

import io.hobaskos.event.eventapp.ui.base.MvpView;

/**
 * Created by andre on 1/26/2017.
 */

public interface EventView<M> extends MvpView {
    @UiThread
    public void showLoading();

    @UiThread
    public void showContent();

    @UiThread
    public void showError(String errorMessage);

    @UiThread
    public void setData(M data);
}

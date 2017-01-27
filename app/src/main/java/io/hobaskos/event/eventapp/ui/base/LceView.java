package io.hobaskos.event.eventapp.ui.base;

import android.support.annotation.UiThread;

/**
 * Created by andre on 1/27/2017.
 *
 * Loading, Content, Error View
 *
 * @param <M> The data model used by the view
 */
public interface LceView<M> extends View {

    // @UiThread: Denotes that the annotated method or constructor
    // should only be called on the UI thread.

    @UiThread
    public void showLoading();

    @UiThread
    public void showContent();

    @UiThread
    public void showError(String errorMessage);

    @UiThread
    public void setData(M data);

}

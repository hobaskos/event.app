package io.hobaskos.event.eventapp.ui.base;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * Created by andre on 1/27/2017.
 */

public class BasePresenter<V extends View> implements Presenter<V> {

    private WeakReference<V> view;

    @UiThread
    @Override
    public void attachView(V view) {
        this.view = new WeakReference<V>(view);
    }

    @UiThread
    @Override
    public void detachView() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    @UiThread
    @Nullable
    public V getView() {
        return view == null ? null : view.get();
    }
}

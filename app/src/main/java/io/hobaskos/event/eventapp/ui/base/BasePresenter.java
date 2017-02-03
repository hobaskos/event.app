package io.hobaskos.event.eventapp.ui.base;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

/**
 * Created by andre on 2/2/2017.
 */

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<V> view;

    @UiThread
    @Override
    public void onViewAttached(V view) {
        this.view = new WeakReference<V>(view);
    }

    @UiThread
    @Override
    public void onViewDetached() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    public void onDestroyed() {
        // TODO
    }

    @UiThread
    @Nullable
    public V getView() {
        return view == null ? null : view.get();
    }

}

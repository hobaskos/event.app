package io.hobaskos.event.eventapp.ui.base;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by andre on 2/2/2017.
 */

public class PresenterLoader<P extends BaseMvpPresenter> extends Loader<P> {

    private final PresenterFactory<P> factory;
    private P presenter;
    private final String tag;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public PresenterLoader(Context context, PresenterFactory<P> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }

    /**
     * Will be called by the Framework for a new or already created Loader
     * once Activity onStart() is reached. In here we check whether we hold
     * a Presenter instance (— in which situation it will be delivered
     * immediately) or the Presenter needs to be created.
     */
    @Override
    protected void onStartLoading() {
        Log.i("loader", "onStartLoading-" + tag);

        // if we already own a presenter instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        // Otherwise, force a load
        forceLoad();
    }

    /**
     * Called when forceLoad() is invoked. Here we are calling the Factory
     * to create our Presenter and delivering the result.
     */
    @Override
    protected void onForceLoad() {
        Log.i("loader", "onForceLoad-" + tag);

        // Create the Presenter using the Factory
        presenter = factory.create();

        // Deliver the result
        deliverResult(presenter);
    }

    /**
     * Delivers our Presenter to the Activity/Fragment.
     * @param data
     */
    @Override
    public void deliverResult(P data) {
        super.deliverResult(data);
        Log.i("loader", "deliverResult-" + tag);
    }


    @Override
    protected void onStopLoading() {
        Log.i("loader", "onStopLoading-" + tag);
    }

    /**
     * will be called before the Loader gets destroyed, giving us the chance
     * to communicate this to the Presenter in case some ongoing operation
     * could be cancelled or additional clean ups would be required.
     */
    @Override
    protected void onReset() {
        Log.i("loader", "onReset-" + tag);
        if (presenter != null) {
            //presenter.onDestroyed();
            presenter = null;
        }
    }
}

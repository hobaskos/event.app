package io.hobaskos.event.eventapp.ui.base.old;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by andre on 2/2/2017.
 */

public abstract class BaseMvpFragment<P extends MvpPresenter>
        extends Fragment {

    private static final String TAG = "base-fragment";
    private static final int LOADER_ID = 102;

    private MvpPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated-" + tag());

        // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader-" + tag());
                return new PresenterLoader<>(getContext(), getPresenterFactory(), tag());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                Log.i(TAG, "onLoadFinished-" + tag());
                BaseMvpFragment.this.presenter = presenter;
                onPresenterPrepared(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                Log.i(TAG, "onLoaderReset-" + tag());
                BaseMvpFragment.this.presenter = null;
                onPresenterDestroyed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart - " + tag());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume-" + tag());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause-" + tag());
    }

    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected abstract String tag();

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link MvpPresenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterPrepared(@NonNull P presenter);

    /**
     * Hook for subclasses before the screen gets destroyed.
     */
    protected void onPresenterDestroyed() {
    }
}

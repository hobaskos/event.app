package io.hobaskos.event.eventapp.ui.base.view.activity;

import android.os.Bundle;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateActivity;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by andre on 2/13/2017.
 */

public abstract class BaseLceViewStateActivity<CV extends View, M, V extends MvpLceView<M>,
        P extends MvpPresenter<V>> extends MvpLceViewStateActivity<CV, M, V, P> {

    @Override protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    protected void injectDependencies() {

    }
}

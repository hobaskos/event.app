package io.hobaskos.event.eventapp.ui.base.view.mvpview;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by andre on 2/13/2017.
 */

public interface MvpLoadMoreView<M> extends MvpLceView<M> {

    void showLoadMore(boolean showLoadMore);
    void showLoadMoreError(Throwable e);
    void addMoreData(M model);
}

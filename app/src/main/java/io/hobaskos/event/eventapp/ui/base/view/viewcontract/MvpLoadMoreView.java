package io.hobaskos.event.eventapp.ui.base.view.viewcontract;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

/**
 * Created by andre on 2/13/2017.
 */

public interface MvpLoadMoreView<M> extends MvpLceView<M> {

    void showLoadMore(boolean showLoadMore);
    void showLoadMoreError(Throwable e);
    void addMoreData(M model);

    // + Methods inherited from MvpLceView:
    //public void showLoading(boolean pullToRefresh);
    //public void showContent();
    //public void showError(Throwable e, boolean pullToRefresh);
    //public void setData(M data);
    //public void loadData(boolean pullToRefresh);

}

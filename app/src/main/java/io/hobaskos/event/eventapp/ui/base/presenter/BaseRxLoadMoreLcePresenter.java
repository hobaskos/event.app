package io.hobaskos.event.eventapp.ui.base.presenter;

import io.hobaskos.event.eventapp.ui.base.view.viewcontract.MvpLoadMoreView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by test on 3/17/2017.
 */

public class BaseRxLoadMoreLcePresenter<M> extends BaseRxLcePresenter<MvpLoadMoreView<M>, M> {

    public final static String TAG = BaseRxLoadMoreLcePresenter.class.getName();


    protected Subscriber<M> loadMoreSubscriber;


    public void subscribeMore(Observable<M> observable) {

        // Show loading in view:
        if (isViewAttached()) {
            getView().showLoadMore(true);
        }

        unsubscribe();

        // Setup subscriber:
        loadMoreSubscriber = new Subscriber<M>() {
            @Override public void onCompleted() {
            }
            @Override  public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showLoadMoreError(e);
                    getView().showLoadMore(false);
                }
            }
            @Override
            public void onNext(M m) {
                if (isViewAttached()) {
                    getView().addMoreData(m);
                    getView().showLoadMore(false);
                }
            }
        };
        // start subscription:
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadMoreSubscriber);
    }

    @Override protected void unsubscribe() {
        super.unsubscribe();
        if (loadMoreSubscriber != null && !loadMoreSubscriber.isUnsubscribed()) {
            loadMoreSubscriber.unsubscribe();
        }
    }
}
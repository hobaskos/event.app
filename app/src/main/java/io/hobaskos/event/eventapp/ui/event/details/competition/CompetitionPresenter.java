package io.hobaskos.event.eventapp.ui.event.details.competition;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import javax.inject.Inject;

/**
 * Created by hans on 23/03/2017.
 */

public class CompetitionPresenter implements MvpPresenter<CompetitionView> {

    private CompetitionView view;

    @Inject
    public CompetitionPresenter() {

    }

    @Override
    public void attachView(CompetitionView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        // do something..
    }

    public void vote(int id) {

    }

}

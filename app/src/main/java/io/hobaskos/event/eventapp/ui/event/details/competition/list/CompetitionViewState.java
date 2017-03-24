package io.hobaskos.event.eventapp.ui.event.details.competition.list;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.CompetitionImage;

/**
 * Created by hans on 24/03/2017.
 */

public class CompetitionViewState extends CastedArrayListLceViewState<List<CompetitionImage>, CompetitionView> {

    public final static String TAG = CompetitionViewState.class.getName();

    public static final Creator<CompetitionViewState> CREATOR = new Creator<CompetitionViewState>() {
        @Override public CompetitionViewState createFromParcel(Parcel source) {
            return new CompetitionViewState(source);
        }

        @Override public CompetitionViewState[] newArray(int size) {
            return new CompetitionViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public CompetitionViewState() {};

    protected CompetitionViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }


    @Override public void apply(CompetitionView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }

}

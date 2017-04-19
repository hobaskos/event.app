package io.hobaskos.event.eventapp.ui.profile.events.mine;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;


/**
 * Created by test on 3/17/2017.
 */

public class MyEventsViewState extends CastedArrayListLceViewState<List<Event>, MyEventsView> {

    public final static String TAG = MyEventsViewState.class.getName();

    public static final Creator<MyEventsViewState> CREATOR = new Creator<MyEventsViewState>() {
        @Override public MyEventsViewState createFromParcel(Parcel source) {
            return new MyEventsViewState(source);
        }

        @Override public MyEventsViewState[] newArray(int size) {
            return new MyEventsViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public MyEventsViewState() {}

    protected MyEventsViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    @Override public void apply(MyEventsView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }

}
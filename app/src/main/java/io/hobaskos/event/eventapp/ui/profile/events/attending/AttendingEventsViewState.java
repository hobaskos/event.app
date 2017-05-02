package io.hobaskos.event.eventapp.ui.profile.events.attending;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by test on 3/19/2017.
 */

public class AttendingEventsViewState extends CastedArrayListLceViewState<List<Event>, AttendingEventsView> {

    public final static String TAG = AttendingEventsViewState.class.getName();

    public static final Creator<AttendingEventsViewState> CREATOR = new Creator<AttendingEventsViewState>() {
        @Override public AttendingEventsViewState createFromParcel(Parcel source) {
            return new AttendingEventsViewState(source);
        }

        @Override public AttendingEventsViewState[] newArray(int size) {
            return new AttendingEventsViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public AttendingEventsViewState() {}

    protected AttendingEventsViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    @Override public void apply(AttendingEventsView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }
}

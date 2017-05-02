package io.hobaskos.event.eventapp.ui.profile.events.archived;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;
import io.hobaskos.event.eventapp.ui.profile.events.attending.AttendingEventsView;
import io.hobaskos.event.eventapp.ui.profile.events.attending.AttendingEventsViewState;

/**
 * Created by test on 3/19/2017.
 */

public class ArchivedEventsViewState extends CastedArrayListLceViewState<List<Event>, ArchivedEventsView> {

    public final static String TAG = AttendingEventsViewState.class.getName();

    public static final Creator<ArchivedEventsViewState> CREATOR = new Creator<ArchivedEventsViewState>() {
        @Override public ArchivedEventsViewState createFromParcel(Parcel source) {
            return new ArchivedEventsViewState(source);
        }

        @Override public ArchivedEventsViewState[] newArray(int size) {
            return new ArchivedEventsViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public ArchivedEventsViewState() {};

    protected ArchivedEventsViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }


    @Override public void apply(ArchivedEventsView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }
}
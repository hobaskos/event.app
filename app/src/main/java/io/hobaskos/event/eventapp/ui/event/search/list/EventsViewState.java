package io.hobaskos.event.eventapp.ui.event.search.list;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 2/13/2017.
 */

public class EventsViewState extends CastedArrayListLceViewState<List<Event>, EventsView> {

    public final static String TAG = EventsViewState.class.getName();

    public static final Creator<EventsViewState> CREATOR = new Creator<EventsViewState>() {
        @Override public EventsViewState createFromParcel(Parcel source) {
            return new EventsViewState(source);
        }

        @Override public EventsViewState[] newArray(int size) {
            return new EventsViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public EventsViewState() {};

    protected EventsViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }


    @Override public void apply(EventsView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }

}

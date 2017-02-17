package io.hobaskos.event.eventapp.ui.event;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.AbsParcelableLceViewState;

import io.hobaskos.event.eventapp.data.model.Event;

/**
 * Created by andre on 16.02.2017.
 */

public class EventViewState extends AbsParcelableLceViewState<Event, EventView> {

    public EventViewState() {};

    public static final Creator<EventViewState> CREATOR = new Creator<EventViewState>() {
        @Override
        public EventViewState createFromParcel(Parcel source) {
            return new EventViewState(source);
        }

        @Override
        public EventViewState[] newArray(int size) {
            return new EventViewState[size];
        }
    };

    protected EventViewState(Parcel in) {
    }

}

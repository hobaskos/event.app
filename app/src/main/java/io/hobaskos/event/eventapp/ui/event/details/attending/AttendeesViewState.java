package io.hobaskos.event.eventapp.ui.event.details.attending;

import android.os.Parcel;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.CastedArrayListLceViewState;

import java.util.List;

import io.hobaskos.event.eventapp.data.model.User;

public class AttendeesViewState extends CastedArrayListLceViewState<List<User>, AttendeesView> {

    public final static String TAG = AttendeesViewState.class.getName();

    public static final Creator<AttendeesViewState> CREATOR = new Creator<AttendeesViewState>() {
        @Override public AttendeesViewState createFromParcel(Parcel source) {
            return new AttendeesViewState(source);
        }

        @Override public AttendeesViewState[] newArray(int size) {
            return new AttendeesViewState[size];
        }
    };

    boolean loadingMore = false;

    // Constructors;
    public AttendeesViewState() {};

    protected AttendeesViewState(Parcel source) {
        super(source);
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }


    @Override public void apply(AttendeesView view, boolean retained) {
        super.apply(view, retained);

        if (currentViewState == STATE_SHOW_CONTENT) {
            view.showLoadMore(loadingMore);
        }
    }
}
